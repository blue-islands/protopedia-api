package jp.livlog.protopedia.api.helper.protopedia;

import lombok.Data;

/**
 * プロトタイプデータ.
 *
 * @author H.Aoshima
 * @version 1.0
 */
@Data
public class ProtoTypeData {

    /** プロトタイプID. */
    private String protoTypeId;

    /** タイトル. */
    private String title;

    /** サムネイル. */
    private String thumb;

    /** Body(HTML). */
    private String bodyHtml;

    /** Body(テキスト). */
    private String bodyText;

    /** リンク. */
    private String link;
}
