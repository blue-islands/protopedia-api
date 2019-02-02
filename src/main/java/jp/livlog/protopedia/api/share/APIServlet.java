package jp.livlog.protopedia.api.share;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import jp.livlog.utility.Symbol;
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
            res.setCharacterEncoding(Symbol.UTF_8);
            res.setContentType("application/json; charset=" + Symbol.UTF_8);
            out = new PrintWriter(new OutputStreamWriter(res.getOutputStream(), Symbol.UTF_8));
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
