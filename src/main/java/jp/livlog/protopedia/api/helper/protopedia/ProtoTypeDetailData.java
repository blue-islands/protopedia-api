package jp.livlog.protopedia.api.helper.protopedia;

import java.util.List;
import java.util.Map;

import lombok.Data;

/**
 * プロトタイプ詳細データ.
 *
 * @author H.Aoshima
 * @version 1.0
 */
@Data
public class ProtoTypeDetailData {

    /** プロトタイプID. */
    private String               protoTypeId;

    /** タイトル. */
    private String               title;

    /** ステータス. */
    private String               status;

    /** Body(HTML). */
    private String               bodyHtml;

    /** Body(テキスト). */
    private String               bodyText;

    /** 動画. */
    private String               video;

    /** イメージ. */
    private List <String>        images;

    /** API・素材等. */
    private List <String>        materials;

    /** タグ. */
    private List <String>        tags;

    /** プロトタイプのURL. */
    private String               url;

    /** チーム名. */
    private String               teamname;

    /** チーム. */
    private Map <String, String> team;

    /** Wow. */
    private String               wow;

    /** 登録日時. */
    private String               timestamp;
}
