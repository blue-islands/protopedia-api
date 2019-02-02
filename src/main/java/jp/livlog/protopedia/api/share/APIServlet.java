package jp.livlog.protopedia.api.share;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.arnx.jsonic.JSON;

/**
 * API用サーブレット.
 *
 * @author H.Aoshima
 * @version 1.0
 *
 */
public abstract class APIServlet extends HttpServlet {

    /** シリアルバージョンUID. */
    private static final long                 serialVersionUID = 1L;

    /** Log. */
    private static Log                        log              = LogFactory.getLog(APIServlet.class);

    /** エラーコード「1 : there is no data」. */
    public static final int                   ERROR_01         = 1;

    /** エラーコード「10 : parameter is required」. */
    public static final int                   ERROR_10         = 10;

    /** エラーコード「11 : Session Timeout」. */
    public static final int                   ERROR_11         = 11;

    /** エラーコード「20 : Invalid or expired token」. */
    public static final int                   ERROR_20         = 20;

    /** エラーコード「21 : Rate limit exceeded」. */
    public static final int                   ERROR_21         = 21;

    /** エラーコード「99 : Abnormal termination」. */
    public static final int                   ERROR_99         = 99;

    /** エラーコードマップ. */
    public static final Map <Integer, String> ERROR_MAP        = new HashMap <>();

    static {
        /** エラーコード「1 : there is no data」. */
        APIServlet.ERROR_MAP.put(ERROR_01, "there is no data");
        /** エラーコード「10 : parameter is required」. */
        APIServlet.ERROR_MAP.put(ERROR_10, "parameter is required");
        /** エラーコード「11 : Session Timeout」. */
        APIServlet.ERROR_MAP.put(ERROR_11, "Session Timeout");
        /** エラーコード「20 : Invalid or expired token」. */
        APIServlet.ERROR_MAP.put(ERROR_20, "Invalid or expired token");
        /** エラーコード「21 : Rate limit exceeded」. */
        APIServlet.ERROR_MAP.put(ERROR_21, "Rate limit exceeded");
        /** エラーコード「99 : Abnormal termination」. */
        APIServlet.ERROR_MAP.put(ERROR_99, "Abnormal termination");
    }


    @Override
    public void doGet(final HttpServletRequest req, final HttpServletResponse res)
            throws ServletException, IOException {

        try {
            this.performTask(req, res);
        } catch (final Exception e) {
            throw new ServletException(e);
        }
    }


    @Override
    public void doPost(final HttpServletRequest req, final HttpServletResponse res)
            throws ServletException, IOException {

        try {
            this.performTask(req, res);
        } catch (final Exception e) {
            throw new ServletException(e);
        }
    }


    /**
     * 実行処理.
     * @param req リクエスト
     * @param res レスポンス
     * @throws ServletException 例外
     * @throws IOException 例外
     */
    protected abstract void performTask(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException;


    /**
     * @param req HttpServletRequest
     * @param res HttpServletResponse
     * @param data   Set <Entry <BigDecimal, String>>
     */
    protected void print(final HttpServletRequest req, final HttpServletResponse res, final Object data) {

        final String json = JSON.encode(data);

        PrintWriter out = null;
        try {
            res.addHeader("Access-Control-Allow-Origin", "*");
            res.setCharacterEncoding(StandardCharsets.UTF_8.name());
            res.setContentType("application/json; charset=" + StandardCharsets.UTF_8.name());
            out = new PrintWriter(new OutputStreamWriter(res.getOutputStream(), StandardCharsets.UTF_8.name()));
            out.print(json);
        } catch (final UnsupportedEncodingException e) {
            APIServlet.log.error(e.getMessage(), e);
        } catch (final IOException e) {
            APIServlet.log.error(e.getMessage(), e);
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }
}
