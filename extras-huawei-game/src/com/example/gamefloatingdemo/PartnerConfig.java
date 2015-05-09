package com.example.gamefloatingdemo;

/**
 *  测试时只需要修改userID、applicationID、devPriKey、devPubKey这四个变量，
 *  若能够调起支付页面，且支付没问题，即可集成
 *	userID为开放联盟的支付id，applicationID为应用id
 *	devPubKey，devPriKey分别为开发者公钥、私钥，从开放联盟上获取
 *
 */
public class PartnerConfig
{
    //游戏浮标的RSA私钥
    public static final String BUOY_PRIVATEKEY =
        "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALDQTtHj3VXtggImeqYGDYklldZK\nazo2Sg2MwXbo/BNBrbSVcDJ6MnteFGgTWhd1lp8/pd2tJbbfS0ELfoV7EqcH7VjC03KSptlHWCpj\nIxOcJj99i8yu343KVg98/qtNv+USPTPblXjVeYxZKxcOyJHGNXwKK5UTK4XLAV2xPyx/AgMBAAEC\ngYBJB7EWllRsUm5ZbwVHn8ZleEW8Pf6uC5BoqlOaRr8fQQh7RyIRZ1GEjSZGmn+ioun0mrhqqEKk\nlvQhSJsYjO31qPL7gBxG5BishSXxQHcwAMr1P7RB9o9ogTU2s3QE3DZQSO48FlRubnPwAssp5cxy\ni4Y8sC/d6BML15swVQSg8QJBAOt8wsD9d/i/7tMFo53+6pYSjvXPXVcETdrzP43mU8YQtdwrZjle\nkUgvE4tVitxtXKCzW2NDQfvo6HQl3qhxq4cCQQDANygrR6fiTUalXRYjmy+AjxaT6/h1ABhGVtSo\nBD3+OubtOXK5gL/cVOYfqD2Wfxq0QlEp+woqhAgYwqlyz2VJAkB+sjUmGDlAACPCLTqGeuxDqeB4\nqASUGKC6uDztX4qa+cqelkr9er+3knx1bqSzS7OWUmlM0pbhrcHDG8zb26xpAkEApge+wcuuX1KC\noFoMworMeE6goPsl7OI9FZzxKYQojE4SpHyH9WYZ09bdxCCNuk5mIaha9VkrydesKr8SoOI2SQJB\nAJF9ktxI0B5W8yXqZgRPUmZ2zwgJv9pkzpB4Q2CjXupaGbcAaxPTbB8fqGiB+lY8HInOKm/wIqvm\nngYKscAfhiI=";
    
    //cpid
    public static final String cpId = "1";
    
    //支付ID。登录http://developer.huawei.com/后，开通支付权益即可获取。
    public static final String userID = "10086000000000293";
    
    // 应用id。在“应用管理”页面点击“创建应用”按钮后，可先保存为草稿之后，即可获取。
    public static final String applicationID = "10129306";
    
    // 开发者联盟上获取的（RSA）私钥,登录http://developer.huawei.com/后，开通支付权益即可获取。
    public static final String RSA_PRIVATE =
        "MIIBVAIBADANBgkqhkiG9w0BAQEFAASCAT4wggE6AgEAAkEAgv/vOAb0oKObkh4YXOryhVrEKoUn36b/esYxdTan0r5scs9HWbdz1MxlEaORltNnQb9Us87oHGI/7D5Uz5nd8QIDAQABAkATVcUwJs6qext2KJz98euTxT7Y68hj2Vkx/NjF7Sg+EYgQouZ5/yMmwQDESaJgTzrmq8KuclhFnxoCiAzhmiYFAiEA3cd5ujeP3xNJ6kU4pTgcGn8It4BAEt8BerwCCo/OND8CIQCXNpHWJ3wAeRihdPBYKkWgE4w1Byy7rNBie2wTXn6hzwIhAJIWrgaORwUozY22H0QmG80QVQubPZmwsGbKpYWTiL89AiBduq6VLy5W4Lkaw3CDRdiYi+VZrVPWFR2qHdT1AJq/0wIgL4lKdiI6fXV0hO8KLQ4KF/WTwtk7pXJWvDuOMtK/GL4=";
    
    // 开发者联盟上获取的（RSA）公钥,登录http://developer.huawei.com/后，开通支付权益即可获取。
    public static final String RSA_PUBLIC =
        "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAIL/7zgG9KCjm5IeGFzq8oVaxCqFJ9+m/3rGMXU2p9K+bHLPR1m3c9TMZRGjkZbTZ0G/VLPO6BxiP+w+VM+Z3fECAwEAAQ==";
}
