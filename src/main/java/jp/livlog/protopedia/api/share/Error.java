package jp.livlog.protopedia.api.share;

import lombok.Data;

/**
 * エラー情報.
 *
 * @author H.Aoshima
 * @version 1.0
 *
 */
@Data
public class Error {

    /** コード. */
    private int    code;

    /** メッセージ. */
    private String message;


    /**
     * コンストラクタ.
     * @param pCode コード
     * @param pMessage メッセージ
     */
    public Error(final int pCode, final String pMessage) {

        this.code = pCode;
        this.message = pMessage;
    }
}
