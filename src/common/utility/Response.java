package common.utility;

import java.io.Serializable;
/**
 * Класс Response.
 * Класс-ответ с сервера.
 */
public class Response implements Serializable {
    /** Поле формат ответа */
    private ResponseCode responseCode;
    /** Поле тело ответа*/
    private String responseBody;

    public Response(ResponseCode responseCode, String responseBody) {
        this.responseCode = responseCode;
        this.responseBody = responseBody;
    }

    /**
     * Запрос формата ответа.
     * @return responseCode - формат кода
     */
    public ResponseCode getResponseCode() {
        return responseCode;
    }

    /**
     * Запрос тела ответа.
     * @return responseBody - тело ответа.
     */
    public String getResponseBody() {
        return responseBody;
    }
}
