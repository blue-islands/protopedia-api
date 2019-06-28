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
import jp.livlog.protopedia.api.share.BreakUtil;
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
     * 検索用のスキップ.
     */
    private static final int    SEARCH_SKIP = 3;


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
        final String protoUrl = ProtoTyper.SUGGETS_URL + "/prototype/" + protoTypeId;
        ProtoTyper.log.info(protoUrl);

        // プロトタイプ一覧を取得
        final Document document = Jsoup.connect(protoUrl).get();
        final Elements title = document.getElementsByTag("h1");
        final Elements status = document.getElementsByClass("field--name-field-status");
        final Elements video = document.getElementsByAttribute("data-video-embed-field-lazy");
        Elements images = document.getElementsByClass("slide");
        if (images.isEmpty()) {
            images = document.getElementsByClass("slick");
        }
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
        // 動画
        if (!video.isEmpty()) {
            try {
                final String videoAttr = video.get(0).attr("data-video-embed-field-lazy");
                final Document document2 = Jsoup.parse(videoAttr);
                final Elements iframeTag = document2.getElementsByTag("iframe");
                final String src = iframeTag.get(0).attr("src");
                data.setVideo(src);
            } catch (final Exception e) {
                log.error(e.getMessage(), e);
            }
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

            final Elements paragraphs = team.get(0).getElementsByClass("paragraph--type--paragraphs-team");
            final Map <String, String> memberMap = new LinkedHashMap <>();
            for (final Element paragraph : paragraphs) {
                final Elements prototyper = paragraph.getElementsByClass("field--name-field-prototyper");
                final Elements role = paragraph.getElementsByClass("field--name-field-roles");
                final Elements prototyperItem = prototyper.get(0).getElementsByClass("field__item");
                if (role.toString().length() > 0) {
                    final Elements roleItem = role.get(0).getElementsByClass("field__item");
                    memberMap.put(prototyperItem.get(0).text(), roleItem.get(0).text());
                } else {
                    memberMap.put(prototyperItem.get(0).text(), "");
                }
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
            final String timestamp = ProtoTyper.getTimestamp(data);
            data.setTimestamp(timestamp);
        } catch (final Exception e) {
            ProtoTyper.log.warn(e.getMessage(), e);
        }

        return data;
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
        final String protoUrl = ProtoTyper.SUGGETS_URL + "/prototyper/" + userId + "?" + parameters.toString();
        ProtoTyper.log.info(protoUrl);

        // プロトタイプ一覧を取得
        final Document document = Jsoup.connect(protoUrl).get();
        final Elements prototypes = document.getElementsByClass("prototypes");
        if (prototypes.size() == 0) {
            throw new NullPointerException();
        }
        final Elements liTags = prototypes.get(0).getElementsByTag("ul").get(0).children();

        ProtoTypeData data = null;
        for (final Element liTag : liTags) {
            final Element title = liTag.getElementsByClass("title").get(0);
            final Element thumb = liTag.getElementsByClass("thumb").get(0);
            final Element body = liTag.getElementsByClass("body").get(0);
            final Element link = liTag.getElementsByClass("link").get(0);
            data = new ProtoTypeData();
            data.setTitle(title.text());
            if (!thumb.children().isEmpty()) {
                data.setThumb(ProtoTyper.SUGGETS_URL + thumb.child(0).attr("src"));
            }
            if (!body.children().isEmpty()) {
                data.setBodyHtml(body.html());
                data.setBodyText(body.text());
            }
            if (!link.children().isEmpty()) {
                data.setLink(ProtoTyper.SUGGETS_URL + link.child(0).attr("href"));
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
     * タイムスタンプを取得する.
     * @param data プロトタイプ詳細データ
     * @return タイムスタンプ
     * @throws Exception 例外
     */
    public static String getTimestamp(final ProtoTypeDetailData data) throws Exception {

        final List <String> list = BreakUtil.convSentenceToRow(data.getBodyText());
        String text = "";
        for (final String val : list) {
            if (val.length() > 0) {
                text = val;
                break;
            }
        }
        if (text.length() > 20) {
            text = text.substring(0, 20);
        }

        // 指定のURLを生成
        final String protoUrl = ProtoTyper.SUGGETS_URL + "/search/" + text;
        ProtoTyper.log.info(protoUrl);

        // 検索結果を取得
        final Document document = Jsoup.connect(protoUrl).get();
        final Element content = document.getElementById("block-protopedia-content");
        final Elements elements = content.children();
        for (int i = ProtoTyper.SEARCH_SKIP; i < elements.size(); i = i + ProtoTyper.SEARCH_SKIP) {
            final Element name = elements.get(ProtoTyper.SEARCH_SKIP);
            // final Element body = list.get(SEARCH_SKIP + 1);
            final Element timestamp = elements.get(ProtoTyper.SEARCH_SKIP + 2);
            if (name.html().indexOf(data.getProtoTypeId()) > -1) {
                final String[] array = timestamp.text().split("-");
                return array[1].trim() + " " + array[2].trim();
            }
        }

        return null;
    }


    /**
     * コンストラクタ.
     */
    private ProtoTyper() {

    }
}
