package jp.livlog.protopedia.api.share;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

/**
 * エラーリスト.
 *
 * @author H.Aoshima
 * @version 1.0
 *
 */
@Data
public class Errors {

    /** メッセージ. */
    private List <Error> errors = new ArrayList <>();
}
