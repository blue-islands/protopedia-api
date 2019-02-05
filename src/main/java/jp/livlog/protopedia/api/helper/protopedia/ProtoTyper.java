package jp.livlog.protopedia.api.helper.protopedia;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import jp.livlog.protopedia.api.servlet.ListServlet;
import jp.livlog.protopedia.api.share.Parameters;

/**
 * プロトタイパークラス.
 *
 * @author H.Aoshima
 * @version 1.0
 */
public final class ProtoTyper {

    /** Log. */
    private static Log          log         = LogFactory.getLog(ListServlet.class);

    /**
     * URL.
     */
    private static final String SUGGETS_URL = "https://protopedia.net";

    /**
     * メンバー取得用のスキップ.
     */
    private static final int    MEMBER_SKIP = 3;

    /**
     * 検索用のスキップ.
     */
    private static final int    SEARCH_SKIP = 3;


    /**
     * コンストラクタ.
     */
    private ProtoTyper() {

    }


    /**
     * プロトタイプ一覧を取得する.
     * @param userId ユーザーID
     * @param page ページ指定
     * @return プロトタイプ一覧
     * @throws Exception 例外
     */
    public static List <ProtoTypeData> getList(final String userId, final int page) throws Exception {

        // 返り値
        final List <ProtoTypeData> list = new ArrayList <>();

        final Parameters parameters = new Parameters();
        parameters.addParameter("page", page);

        // 指定のURLを生成
        final String protoUrl = SUGGETS_URL + "/prototyper/" + userId + "?" + parameters.toString();
        log.info(protoUrl);

        // プロトタイプ一覧を取得
        final Document document = Jsoup.connect(protoUrl).get();
        final Elements prototypes = document.getElementsByClass("prototypes");
        if (prototypes.size() == 0) {
            throw new NullPointerException();
        }
        final Elements liTags = prototypes.get(0).getElementsByTag("li");

        ProtoTypeData data = null;
        for (final Element liTag : liTags) {
            final Element title = liTag.getElementsByClass("title").get(0);
            final Element thumb = liTag.getElementsByClass("thumb").get(0);
            final Element body = liTag.getElementsByClass("body").get(0);
            final Element link = liTag.getElementsByClass("link").get(0);
            data = new ProtoTypeData();
            data.setTitle(title.text());
            if (!thumb.children().isEmpty()) {
                data.setThumb(SUGGETS_URL + thumb.child(0).attr("src"));
            }
            if (!body.children().isEmpty()) {
                data.setBody(body.html());
            }
            if (!link.children().isEmpty()) {
                data.setLink(SUGGETS_URL + link.child(0).attr("href"));
                final String[] linls = data.getLink().split("/");
                data.setProtoTypeId(linls[linls.length - 1]);
            }
            list.add(data);
        }

        if (list.size() == 0) {
            throw new NullPointerException();
        }

        return list;
    }


    /**
     * プロトタイプ詳細情報を取得する.
     * @param protoTypeId プロトタイプID
     * @return プロトタイプ情報
     * @throws Exception 例外
     */
    public static ProtoTypeDetailData getDetail(final String protoTypeId) throws Exception {

        // 返り値
        ProtoTypeDetailData data = null;

        // 指定のURLを生成
        final String protoUrl = SUGGETS_URL + "/prototype/" + protoTypeId;
        log.info(protoUrl);

        // プロトタイプ一覧を取得
        final Document document = Jsoup.connect(protoUrl).get();
        final Elements title = document.getElementsByTag("h1");
        final Elements status = document.getElementsByClass("field--name-field-status");
        final Elements images = document.getElementsByClass("slide");
        final Elements summary = document.getElementsByClass("field--type-text-with-summary");
        final Elements materials = document.getElementsByClass("field--name-field-materials");
        final Elements tags = document.getElementsByClass("field--name-field-prototype-tags");
        final Elements url = document.getElementsByClass("field--name-field-url");
        final Elements teamname = document.getElementsByClass("field--name-field-teamname");
        final Elements team = document.getElementsByClass("field--name-field-team");
        final Elements wow = document.getElementsByClass("field--name-field-wow");

        data = new ProtoTypeDetailData();
        // タイトル
        if (!title.isEmpty()) {
            final String titleText = title.get(0).text();
            data.setTitle(titleText);
        }
        // ステータス
        if (!status.isEmpty()) {
            final String statusText = status.get(0).text();
            data.setStatus(statusText);
        }
        // イメージ
        if (!images.isEmpty()) {
            final List <String> imageList = new ArrayList <>();
            for (final Element image : images) {
                final Elements imagetags = image.getElementsByTag("a");
                for (final Element imagetag : imagetags) {
                    imageList.add(imagetag.attr("href"));
                }
            }
            data.setImages(imageList);
        }
        // Body
        if (!summary.isEmpty()) {
            data.setBodyHtml(summary.get(0).html());
            data.setBodyText(summary.get(0).text());
        }
        // API・素材等
        if (!materials.isEmpty()) {
            final Elements materialItems = materials.get(0).getElementsByClass("field__item");
            final List <String> materialList = new ArrayList <>();
            for (final Element materialItem : materialItems) {
                materialList.add(materialItem.text());
            }
            data.setMaterials(materialList);
        }
        // タグ
        if (!tags.isEmpty()) {
            final Elements tagItems = tags.get(0).getElementsByClass("field__item");
            final List <String> tagList = new ArrayList <>();
            for (final Element tagItem : tagItems) {
                tagList.add(tagItem.text());
            }
            data.setTags(tagList);
        }

        // URL
        if (!url.isEmpty()) {
            data.setUrl(url.get(0).text());
        }
        // チーム名
        if (!teamname.isEmpty()) {
            final Elements teamnameItem = teamname.get(0).getElementsByClass("field__item");
            if (!teamnameItem.isEmpty()) {
                data.setTeamname(teamnameItem.get(0).text());
            }
        }
        // チーム
        if (!team.isEmpty()) {
            final Elements member = team.get(0).getElementsByClass("field__item");
            final Map <String, String> memberMap = new LinkedHashMap <>();
            for (int i = 0; i < member.size(); i = i + MEMBER_SKIP) {
                final Element name = member.get(i + 1);
                final Element part = member.get(i + 2);
                memberMap.put(name.text(), part.text());
            }
            data.setTeam(memberMap);
        }
        // Wow
        if (!wow.isEmpty()) {
            final Elements wowItem = wow.get(0).getElementsByClass("field__item");
            if (!wowItem.isEmpty()) {
                data.setWow(wowItem.get(0).text());
            }
        }
        // プロトタイプID
        data.setProtoTypeId(protoTypeId);

        // タイムスタンプ
        try {
            final String timestamp = getTimestamp(data.getTitle(), protoTypeId);
            data.setTimestamp(timestamp);
        } catch (final Exception e) {
            log.warn(e.getMessage(), e);
        }

        return data;
    }


    /**
     * タイムスタンプを取得する.
     * @param title タイトル
     * @param protoTypeId プロトタイプID
     * @return タイムスタンプ
     * @throws Exception 例外
     */
    public static String getTimestamp(final String title, final String protoTypeId) throws Exception {

        // 指定のURLを生成
        final String protoUrl = SUGGETS_URL + "/search/" + title;
        log.info(protoUrl);

        // 検索結果を取得
        final Document document = Jsoup.connect(protoUrl).get();
        final Element content = document.getElementById("block-protopedia-content");
        final Elements list = content.children();
        for (int i = SEARCH_SKIP; i < list.size(); i = i + SEARCH_SKIP) {
            final Element name = list.get(SEARCH_SKIP);
            // final Element body = list.get(SEARCH_SKIP + 1);
            final Element timestamp = list.get(SEARCH_SKIP + 2);
            if (name.html().indexOf(protoTypeId) > -1) {
                final String[] array = timestamp.text().split("-");
                return array[1].trim() + " " + array[2].trim();
            }
        }

        return null;
    }
}
