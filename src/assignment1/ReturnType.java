package assignment1;

public class ReturnType {
    public enum ResultType {
        NORMAL, BREAK, CONTINUE, RETURN
    }

    private final ResultType resultType;
    private final String value;

    // 构造器：只有枚举
    public ReturnType(ResultType resultType) {
        this.resultType = resultType;
        this.value = null;
    }

    // 构造器：枚举和字符串
    public ReturnType(ResultType resultType, String value) {
        this.resultType = resultType;
        this.value = value;
    }

    public ResultType getResultType() {
        return resultType;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "ReturnType{" +
                "resultType=" + resultType +
                ", value='" + value + '\'' +
                '}';
    }
}

