package com.nus.team4.constant;

/**
 * FileName: AuthorityConstant.java
 *
 * @author 刘梓健
 * @version 1.0
 * @Description 授权需要使用的常量信息
 * @Date 2024/2/5 14:32
 */
public class AuthorityConstant {

//    RSA私钥，除了授权中心，不暴漏给任何客户端
    public final static String PRIVATE_KEY = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggS" +
            "jAgEAAoIBAQCA5AYTk2N4lLMi8JTYVgM7xdBA13M8pxO41DDEH4epqWdXbOr9IcK73mN6q+" +
            "NLH3/orShE77zvp3E+pNYueis3jdaBaPSW1doRUjldyj9s0hqhWQmH1ba7QJBHM5Fo6c8EK" +
            "2iLy3NAmC3vc5GeStY/1dlCP4pm2fy5nxrDmqpCnLDH0Cqb2Paw0+29Spa0m+5vU3KectcY" +
            "wAuynlhANc0cQ8X50hjEUrxTUzGBujaztoFhztwnULnfjor3G4Yy9uyHIJHNVX8/L42NAaE" +
            "CqIdbe8QyS9WfsvYtHKASWItbYrwH9ZOiccRaQ0cOBWKmkWdFOIYLrFZtTHoxdp2QUQMlAg" +
            "MBAAECggEAaQNfwAtR/CiLW2hQnYJW2BhveKURitVFyE15/hECY0VWoXBodR1Qn+itfIEXi" +
            "1l6yYRrTt9VPJ1cMwHyT6PSGk1B++Ybgdly9rHbc4MY8fytHdCBZ17EpwQPiTOGwcfdlzQW" +
            "1e9JU9OpeXLbrfCjZxdBECzdAreQpVvwXnZhSitf/9+St3/Tx0HX+oehNg7L+C5VZhuhGXe" +
            "dFDIXZZiCeaXNzJI72/6Z2xGKrHfzykrbcRp40RyHMnplL9p6G4nA5I6hA2ceLTV8BPwYIi" +
            "/UeB7hWBwSvg+zV4aG3+B3J2TTZ1FHWnLElOjuP4FpEgaUX0f+HPSAwnvgCTp2LPtagQKBg" +
            "QC5Iaa3J2dX1i0isJXkddG/KWM5HhQMe1erywm1cm+mFyzxkrErgvvi5e5t2y5DNham0gd7" +
            "FdoFcOUmQU5ZVHM5T5Ba0feXr6cAcl8seQI4WiqJ15lwnzr9lJLVBrmuEbidChk+719G4DF" +
            "d2x4462nzw6C56GpWeqjeHCAMU+mhGQKBgQCyOvFYMF7tC4bxbAUNFLH5FSPN7tPawc2Zqw" +
            "WrfiFmAexuVDgv3fgQvw3c4OQgvSCkLK3/iJtZ+hfqLK8nM8JLEoPHviu/dYLeZ5fdKLUtd" +
            "shLN1Op0cxTdl/NWrYrb8UvrOPfvMj36EaOnx+A7wfkjGhE2vckVRLxJji1+4637QKBgH7y" +
            "B0qyF1gQwhR2d+IXtELb03EYIfFJJp3jdP3ZwZtnaucfiqNmhuhYeH0V/YwS68dqX7zUCBM" +
            "RDeCEiYARu40B9N+B+MAnr2FMC90IATC5C1BH6axjU2UM2v5+RRaKikgM47DXHUMRemh8Vy" +
            "X6r0V60JyKNVildWQQbx1k8OAJAoGBAIe8GgeWtwSCbd+461Pd+fh/YAg6pb+JL0Kjm6rSY" +
            "S8lseEIZM4Xlw9x4WXhI/vG7W/60vy0FdP9gm8GXtJVOdggnpnlO74RCXzFmkOE+A5K0xlt" +
            "ZlsDez/o++cR+YC1G5BGdDj1tnwOuoVtq/lukS0+vT4LIILcA5QCDtiRX0y1AoGAJ3BQJj9" +
            "fM1XkukT7gdXhctkwTeF/uo2q6Jm6gbH3A7BTCD2DauBzD/XRRPpHA4TMxNzhvzFhrTmtsA" +
            "FCMz4PTp39B4rgfHVv1krC1cjsIGS2bnHiFHanf4GKPB+/ZYI3IBVrR59kM6EHySwsi5S+G" +
            "Pp6ciqvdswIgw/oGjcs2WA=";

//    默认的JWT token超时时间
    public final static Integer DEFAULT_EXPIRE_MINUTE = 10;

    //    RSA公钥
    public final static String PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCA" +
            "QEAgOQGE5NjeJSzIvCU2FYDO8XQQNdzPKcTuNQwxB+HqalnV2zq/SHCu95jeqvjSx9/6K0oRO+876" +
            "dxPqTWLnorN43WgWj0ltXaEVI5Xco/bNIaoVkJh9W2u0CQRzORaOnPBCtoi8tzQJgt73ORnkrWP9X" +
            "ZQj+KZtn8uZ8aw5qqQpywx9Aqm9j2sNPtvUqWtJvub1NynnLXGMALsp5YQDXNHEPF+dIYxFK8U1Mx" +
            "gbo2s7aBYc7cJ1C5346K9xuGMvbshyCRzVV/Py+NjQGhAqiHW3vEMkvVn7L2LRygEliLW2K8B/WTo" +
            "nHEWkNHDgVippFnRTiGC6xWbUx6MXadkFEDJQIDAQAB";

    //    JWT中存储用户信息的key
    public final static String JWT_USER_INFO_KEY = "bank-user";
}
