package jp.livlog.protopedia.api.helper.protopedia;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import jp.livlog.protopedia.api.share.Parameters;

/**
 * プロトタイパークラス.
 *
 * @author H.Aoshima
 * @version 1.0
 */
public final class ProtoTyper {

    /** Log. */
    private static Logger       log         = Logger.getLogger(ProtoTyper.class.getName());

    /**
     * URL.
     */
    private static final String SUGGETS_URL = "https://protopedia.net";


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
        final String url = SUGGETS_URL + "/prototyper/" + userId + "?" + parameters.toString();
        log.log(Level.INFO, url);

        // プロトタイプ一覧を取得
        final Document document = Jsoup.connect(url).get();
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
                data.setBody(body.child(0).html());
            }
            if (!link.children().isEmpty()) {
                data.setLink(SUGGETS_URL + link.child(0).attr("href"));
            }
            list.add(data);
        }

        if (list.size() == 0) {
            throw new NullPointerException();
        }

        return list;
    }

}
