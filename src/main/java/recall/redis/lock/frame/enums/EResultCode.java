package recall.redis.lock.frame.enums;

/**
 * 结果code枚举
 */
public enum EResultCode {

    /**
     * 成功
     */
    SUCCESS("200"),

    /**
     * 失败
     */
    ERROR("500");

    public String code;

    EResultCode(String code) {
        this.code = code;
    }
}
