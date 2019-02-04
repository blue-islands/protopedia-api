package jp.livlog.protopedia.api.servlet;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import jp.livlog.protopedia.api.helper.protopedia.ProtoTypeDetailData;
import jp.livlog.protopedia.api.helper.protopedia.ProtoTyper;
import jp.livlog.protopedia.api.share.APIServlet;

/**
 * プロトタイプ詳細取得サーブレット.
 *
 * @author H.Aoshima
 * @version 1.0
 *
 */
@WebServlet (name = "prototype", urlPatterns = { "/prototype" })
public class DetailServlet extends jp.livlog.protopedia.api.share.APIServlet {

    /** シリアルバージョンUID. */
    private static final long serialVersionUID = 1L;

    /** Log. */
    private static Log        log              = LogFactory.getLog(DetailServlet.class);


    @Override
    protected void performTask(final HttpServletRequest req, final HttpServletResponse res) throws ServletException, IOException {

        // 返り値
        final Map <String, Object> ret = new HashMap <>();
        final jp.livlog.protopedia.api.share.Errors errors = new jp.livlog.protopedia.api.share.Errors();

        // +----- パラメータの取得 -----+
        req.setCharacterEncoding(StandardCharsets.UTF_8.name());

        // ユーザーID
        final String prototype = req.getParameter("prototype");

        if (StringUtils.isEmpty(prototype)) {
            errors.getErrors().add(new jp.livlog.protopedia.api.share.Error(ERROR_10, APIServlet.ERROR_MAP.get(ERROR_10)));
            this.print(req, res, errors);
            return;
        }

        try {

            try {
                final ProtoTypeDetailData data = ProtoTyper.getDetail(prototype);
                ret.put("prototype", data);
            } catch (final Exception e) {
                errors.getErrors().add(new jp.livlog.protopedia.api.share.Error(ERROR_01, APIServlet.ERROR_MAP.get(ERROR_01)));
                this.print(req, res, errors);
                return;
            }

            this.print(req, res, ret);

        } catch (final Exception e) {
            DetailServlet.log.error(e.getMessage(), e);
            errors.getErrors().add(new jp.livlog.protopedia.api.share.Error(ERROR_99, APIServlet.ERROR_MAP.get(ERROR_99)));
            this.print(req, res, errors);
        }

    }
}
