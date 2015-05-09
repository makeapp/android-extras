;(function(win, doc, undefined) {
    var utils = {};
    var sspData = {};
    var sspbridge = {};
    var toString = Object.prototype.toString;
    var isType = function(type) {
        return function(o) {
            return toString.call(o) === '[object ' + type + ']';
        };
    };

    utils.mix = function(r, s, ov, wl) {
        if (!s || !r) {
            return r;
        }
        if (ov === undefined) {
            ov = true;
        }
        var i, p, l;
        if (wl && (l = wl.length)) {
            for (i = 0; i < l; i++) {
                p = wl[i];
                if (p in s) {
                    if (ov || !(p in r)) {
                        r[p] = s[p];
                    }
                }
            }
        } else {
            for (p in s) {
                if (ov || !(p in r)) {
                    r[p] = s[p];
                }
            }
        }
        return r;
    }

    utils.mix(utils, {
        toArry: function(o) {
            return Array.prototype.slice.call(o, 0)
        },
        //工具函数 是否为函数
        iF: isType('Function'),

        //工具函数 是否为数组
        iA: isType('Array'),

        //工具函数 是否为字符串
        iS: isType('String'),

        //工具函数 是否为对象
        iO: isType('Object'),

        //是否为纯对象, 排除dom节点及window
        iPO: function(o) {
            return o && S.iO(o) && !o.nodeType && !o.setInterval;
        },
        each: function(arry, fn) {
            if (arry.length && arry.slice) {
                for (var i = 0, len = arry.length; i < len; i++) {
                    fn(arry[i], i)
                }
            } else {
                for (var key in arry) {
                    if (arry.hasOwnProperty(key)) {
                        fn(arry[key], key)
                    }
                }
            }
        },
        trim: function(str) {
            return str.replace(/^\s+|\s+$/g, "");
        },
        oTStr: function(obj){
            if (utils.iO(obj)) {
                var ret = [];
                utils.each(obj, function(v, k){
                    ret.push(k + '+' + v)
                })
                return encodeURIComponent(ret.join('&'))
            }
            return obj;
        }
    })

    //事件处理
    var evt = {}
    utils.mix(evt, {
        //使一个对象拥有自定义事件的能力
        //给对象添加下面的方法和属性
        //addEventListener(name, cb)
        //fireEvent(name)
        //removeEventListener(name[, cb])
        //eventsListeners - > array
        enableEvent: function(obj) {
            if (obj.__enableEvent) {
                return obj
            }

            //事件对象
            var listeners = obj.__eventsListeners = {}

            obj.addEventListener = function(name, cb) {
                if (!listeners[name]) {
                    listeners[name] = []
                }

                listeners[name].push(cb)
            }

            obj.fireEvent = function(name, data) {
                //data可以接收对象，也可以是一个参数数组
                if (listeners[name]) {
                    utils.each(listeners[name], function(v, i) {
                        v.apply(null, utils.iA(data) ? data : [data])
                    })
                }
            }

            obj.removeEventListener = function(name, cb) {
                if (listeners[name]) {
                    //没有cb的时候清除所有name的函数
                    if (!cb) {
                        delete listeners[name]
                    } else {
                        for (var i = 0; i < listeners[name].length; i++) {
                            if (listeners[name][i] === cb) {
                                listeners[name].splice(i, 1)
                            }
                        }
                    }
                }
            }

            obj.__enableEvent = true
            return obj
        }
    });
    evt.enableEvent(sspbridge);
    (function() {
        var ua = win.navigator.userAgent;
        var isIOS = /iPhone|iPad|iPod/i.test(ua);
        var isAndroid = /Android/i.test(ua);
        var osVersion = ua.match(/(?:OS|Android)[\/\s](\d+[._]\d+(?:[._]\d+)?)/i);
        var hasOwnProperty = Object.prototype.hasOwnProperty;
        var callbackMap = {}, inc = 1,
            iframePool = [],
            iframeLimit = 3;
        var LOCAL_PROTOCOL = "ssp";
        var PROTOCOL = "ssp";
        var IFRAME_PREFIX = "ssp_iframe_";
        var SUCCESS_PREFIX = "suc_";
        var FAILURE_PREFIX = "err_";
        var PARAM_PREFIX = "param_";

        function compareVersion(v1, v2) {
            v1 = v1.toString().split(".");
            v2 = v2.toString().split(".");
            for (var i = 0; i < v1.length || i < v2.length; i++) {
                var n1 = parseInt(v1[i], 10),
                    n2 = parseInt(v2[i], 10);
                if (win.isNaN(n1)) {
                    n1 = 0
                }
                if (win.isNaN(n2)) {
                    n2 = 0
                }
                if (n1 < n2) {
                    return -1
                } else if (n1 > n2) {
                    return 1
                }
            }
            return 0
        }

        function callback(func, param) {
            if (isAndroid && compareVersion(osVersion, "2.4.0") < 0) {
                setTimeout(function() {
                    func && func(param.value || param)
                }, 1)
            } else {
                func && func(param.value || param)
            }
        }
        if (osVersion) {
            osVersion = (osVersion[1] || "0.0.0").replace(/\_/g, ".")
        } else {
            osVersion = "0.0.0"
        }
        var WV_Core = {
            call: function(obj, method, param, successCallback, failureCallback, timeout) {
                var sid;
                if (timeout > 0) {
                    sid = setTimeout(function() {
                        WV_Core.onFailure(sid, {
                            ret: "TIMEOUT"
                        })
                    }, timeout)
                } else {
                    sid = WV_Private.getSid()
                }
                WV_Private.registerCall(sid, successCallback, failureCallback);
                if (isAndroid) {
                    WV_Private.callMethodByPrompt(obj, method, WV_Private.buildParam(param), sid + "")
                } else if (isIOS) {
                    WV_Private.callMethodByIframe(obj, method, WV_Private.buildParam(param), sid + "")
                }
            },
            getParam: function(sid) {
                return WV_Private.params[PARAM_PREFIX + sid] || ""
            },
            onSuccess: function(sid, msg) {
                clearTimeout(sid);
                var func = WV_Private.unregisterCall(sid).success,
                    param = WV_Private.parseParam(msg);
                callback(func, param);
                WV_Private.onComplete(sid)
            },
            onFailure: function(sid, msg) {
                clearTimeout(sid);
                var func = WV_Private.unregisterCall(sid).failure,
                    param = WV_Private.parseParam(msg);
                callback(func, param);
                WV_Private.onComplete(sid)
            }
        };
        var WV_Private = {
            params: {},
            buildParam: function(obj) {
                if (obj && typeof obj === "object") {
                    return JSON.stringify(obj)
                } else {
                    return obj || ""
                }
            },
            parseParam: function(str) {
                if (str && typeof str === "string") {
                    try {
                        obj = JSON.parse(str)
                    } catch (e) {
                        obj = eval("(" + str + ")")
                    }
                } else {
                    obj = str || {}
                }
                return obj
            },
            getSid: function() {
                return Math.floor(Math.random() * (1 << 50)) + "" + inc++
            },
            registerCall: function(sid, successCallback, failedCallback) {
                if (successCallback) {
                    callbackMap[SUCCESS_PREFIX + sid] = successCallback
                }
                if (failedCallback) {
                    callbackMap[FAILURE_PREFIX + sid] = failedCallback
                }
            },
            unregisterCall: function(sid) {
                var sucId = SUCCESS_PREFIX + sid,
                    failId = FAILURE_PREFIX + sid,
                    call = {
                        success: callbackMap[sucId],
                        failure: callbackMap[failId]
                    };
                delete callbackMap[sucId];
                delete callbackMap[failId];
                return call
            },
            useIframe: function(sid, url) {
                var iframeid = IFRAME_PREFIX + sid,
                    iframe = iframePool.pop();
                if (!iframe) {
                    iframe = doc.createElement("iframe");
                    iframe.setAttribute("frameborder", "0");
                    iframe.style.cssText = "width:0;height:0;border:0;display:none;"
                }
                iframe.setAttribute("id", iframeid);
                iframe.setAttribute("src", url);
                if (!iframe.parentNode) {
                    setTimeout(function() {
                        doc.body.appendChild(iframe)
                    }, 5)
                }
            },
            retrieveIframe: function(sid) {
                var iframeid = IFRAME_PREFIX + sid,
                    iframe = doc.getElementById(iframeid);
                if (iframePool.length >= iframeLimit) {
                    doc.body.removeChild(iframe)
                } else {
                    iframePool.push(iframe)
                }
            },
            callMethodByIframe: function(obj, method, param, sid) {
                var src = LOCAL_PROTOCOL + "://" + obj + ":" + sid + "/" + method + "?" + param;
                this.useIframe(sid, src)
            },
            callMethodByPrompt: function(obj, method, param, sid) {
                var title = LOCAL_PROTOCOL + "://" + obj + ":" + sid + "/" + method + "?" + param;
                var val = PROTOCOL + ":";
                win.prompt(title, val)
            },
            onComplete: function(sid) {
                if (isIOS) {
                    this.retrieveIframe(sid)
                }
            }
        };
        for (var key in WV_Core) {
            if (!hasOwnProperty.call(sspbridge, key)) {
                sspbridge[key] = WV_Core[key]
            }
        }
        sspbridge.nativeCall = function(method, params, cb) {
            sspbridge.call("AlimamaMraid", method, params, cb)
        }
        win.WindVane = WV_Core;
    })();
    utils.each(["creativeReady", "layerChange"], function(v, i) {
        sspbridge.addEventListener(v, function(message, action) {
            var data = utils.toArry(arguments);
            ssp.fireEvent(v, data);
        })
    });
    var ssp = {};
    win.activeCreativeId = '';
    evt.enableEvent(ssp);

    function genSspCallFunc(func) {
        return function() {
            var arryArguments = utils.toArry(arguments);
            // var _iframeId;
            // if (arguments.length > func.length) {
            //   _iframeId = arryArguments.pop()
            // } else if (arguments.length == func.length) {
            //   _iframeId = iframeId
            // }
            var params = func.apply(null, arryArguments);
            if (params && utils.iA(params)) {
                if (!params[1]) {
                    params[1] = {}
                }
                params[1]._iframeId = activeCreativeId;
                sspbridge.nativeCall.apply(null, params)
            }
        }
    }

    //native 方法
    utils.mix(ssp, {
        creativeShow: genSspCallFunc(function() {
            return ['creativeShow']
        }),
        generateToken: genSspCallFunc(function(success) {
            if (utils.iF(success)) {
                return ['generateToken',{},success]
            } else {
                return ['generateToken']
            }
        }),
        downloadFeedback: genSspCallFunc(function(data){
            return ['downloadFeedback', data]
        })
    })

    //js 方法
    utils.mix(ssp, {
        getLayerState: function(){
            return sspData.layerState || 'front';
        },
        generateFeedbackUrl: function(json, action, url, touchTime, openTime, impressionDate){
            var g = function(token, st, ct, tn) {
                var s, cs, t1, m;
                s = 0; // 防伪值
                cs = token;
                if (0 == cs.length) {
                    return s;
                }
                t1 = (ct>0?ct:0);
                m = (tn>0? tn:0);
                for (var i=0;i<((t1+st)%(0x409*0225&73));++i) {
                    idx = (i*m)% cs.length;
                    s+= cs.charCodeAt(idx);
                }
                return s;
            }
            var sz = mraid.getSize();
            var f1 = 1; // 版本号
            var f3 = action;
            var f4 = Date.now();
            var f5 = impressionDate;  //
            var f6 =  (json['info'] ? json['info']['orientation'] : 0) || win.orientation || -1
            //广告位宽度
            var f7 = sz.width;
            //广告位高度
            var f8 = sz.height;
            var f9 = touchTime;
            var f10 = openTime;
            var f2 = g(json['token'], f4-f5, f4, f9);
            json.ts = f4
            json.sc = f1 + "," + f2 + "," + f3 + "," + f4 + "," + f5 + "," + f6 + "," + f7 + "," + f8 + "," + f9 + "," + f10

            // alert(url + '-----' + url.replace(/@[^@]+@/g,function(match){
                // return fm[match] || '';
            // }));
            var ret = url.replace(/@([^@]+)@/g,function(match, key){
                return json[key] || '';
            });
            console.log('RRRRRRRRRRRRRRRRRRRRR'+ret)
            return ret;
        },
        imgLog: function(url) {
            if (!url) return;
            var img = document.createElement('img');
            img.src = url;
            setTimeout(function() {
                img = null;
            }, 100)
            // alert('img---------' + url)
            console.info('img log request:' + url);
        },
        ajaxLog: function(header, url) {
            if (!url) return;
            var xmlhttp = new XMLHttpRequest();
            xmlhttp.open('GET', url, true);
            utils.each(header, function(v, key){
                xmlhttp.setRequestHeader(key, v);
            })
            xmlhttp.send();
            // alert('ajax------'+url)
            console.info('ajax log request:' + url);
        }
    })
    //默认 ssp事件
    var listeners = {
        creativeReady: function(data) {
            activeCreativeId = data['_iframeId'];
        },
        layerChange: function(state){
            console.log('<<<<<<<<<<',state)
            sspData.layerState = state;
        }
    }
    utils.each(listeners, function(v, key) {
        ssp.addEventListener(key, v)
    });
    win.ssp = ssp;
    // win.activeCreativeId = activeCreativeId;
    win.sspbridge = sspbridge;
})(window, document);