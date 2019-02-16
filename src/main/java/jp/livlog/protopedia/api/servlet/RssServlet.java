package jp.livlog.protopedia.api.servlet;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.rometools.rome.feed.synd.SyndContent;
import com.rometools.rome.feed.synd.SyndContentImpl;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndEntryImpl;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.feed.synd.SyndFeedImpl;
import com.rometools.rome.io.SyndFeedOutput;

import jp.livlog.protopedia.api.helper.protopedia.ProtoTypeData;
import jp.livlog.protopedia.api.helper.protopedia.ProtoTyper;

/**
 * RSS取得サーブレット.
 *
 * @author H.Aoshima
 * @version 1.0
 *
 */
@WebServlet (name = "rss", urlPatterns = { "/rss" })
public class RssServlet extends jp.livlog.protopedia.api.share.APIServlet {

    /** シリアルバージョンUID. */
    private static final long     serialVersionUID = 1L;

    /** Log. */
    private static Log            log              = LogFactory.getLog(RssServlet.class);

    /** RSS type. */
    private static final String[] RSS_TYPE         = {
            "rss_1.0",
            "rss_2.0",
            "rss_0.9",
            "rss_0.92",
            "atom_1.0",
            "atom_0.3" };


    @Override
    protected void performTask(final HttpServletRequest req, final HttpServletResponse res) throws ServletException, IOException {

        new HashMap <>();
        new jp.livlog.protopedia.api.share.Errors();

        // +----- パラメータの取得 -----+
        req.setCharacterEncoding(StandardCharsets.UTF_8.name());

        // ユーザーID
        final String user = req.getParameter("user");

        if (StringUtils.isEmpty(user)) {
            this.print(req, res, "");
            return;
        }

        try {

            final List <ProtoTypeData> list = ProtoTyper.getList(user, 0);

            // フィードの全体情報を設定
            final SyndFeed feed = new SyndFeedImpl();
            feed.setLink("https://protopedia.net/prototyper/" + user);
            feed.setTitle(user + " prototypes");
            feed.setDescription("ProtoPedia");
            feed.setFeedType(RssServlet.RSS_TYPE[1]);

            // フィードの記事を作成
            final List <SyndEntry> entries = new ArrayList <>();
            // for (int i = 0; i < 10; i++) {
            for (final ProtoTypeData data : list) {

                // 1つ分の記事を作成
                final SyndEntry entry = new SyndEntryImpl();
                final SyndContent desc = new SyndContentImpl();
                entry.setDescription(desc);

                entry.setTitle(data.getTitle());
                entry.setLink(data.getLink());
                desc.setType("text/plain");
                desc.setValue(data.getBodyText());

                // 一覧に記事を追加
                entries.add(entry);
            }

            // 記事一覧をフィードに追加
            feed.setEntries(entries);

            this.print(req, res, new SyndFeedOutput().outputString(feed));

        } catch (final Exception e) {
            RssServlet.log.error(e.getMessage(), e);
            this.print(req, res, "");
        }

    }


    /**
     * @param req HttpServletRequest
     * @param res HttpServletResponse
     * @param data   Set <Entry <BigDecimal, String>>
     */
    protected void print(final HttpServletRequest req, final HttpServletResponse res, final String data) {

        PrintWriter out = null;
        try {
            res.addHeader("Access-Control-Allow-Origin", "*");
            res.setCharacterEncoding(StandardCharsets.UTF_8.name());
            res.setContentType("application/json; charset=" + StandardCharsets.UTF_8.name());
            out = new PrintWriter(new OutputStreamWriter(res.getOutputStream(), StandardCharsets.UTF_8.name()));
            out.print(data);
        } catch (final UnsupportedEncodingException e) {
            RssServlet.log.error(e.getMessage(), e);
        } catch (final IOException e) {
            RssServlet.log.error(e.getMessage(), e);
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }
}
