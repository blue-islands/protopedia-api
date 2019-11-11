package jp.livlog.protopedia.api.servlet;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import jp.livlog.protopedia.api.helper.protopedia.ProtoTypeData;
import jp.livlog.protopedia.api.helper.protopedia.ProtoTyper;
import jp.livlog.protopedia.api.share.APIServlet;

/**
 * プロトタイプリスト取得サーブレット.
 *
 * @author H.Aoshima
 * @version 1.0
 *
 */
@WebServlet (name = "prototypes", urlPatterns = { "/prototypes" })
public class ListServlet extends jp.livlog.protopedia.api.share.APIServlet {

    /** シリアルバージョンUID. */
    private static final long serialVersionUID = 1L;

    /** Log. */
    private static Log        log              = LogFactory.getLog(ListServlet.class);

    // /** 除外文字. */
    // private static final String EXCLUSION_CHARACTER = "_.'@";


    @Override
    protected void performTask(final HttpServletRequest req, final HttpServletResponse res) throws ServletException, IOException {

        // 返り値
        final Map <String, Object> ret = new HashMap <>();
        final jp.livlog.protopedia.api.share.Errors errors = new jp.livlog.protopedia.api.share.Errors();

        // +----- パラメータの取得 -----+
        req.setCharacterEncoding(StandardCharsets.UTF_8.name());

        // ユーザーID
        final String user = req.getParameter("user");

        if (StringUtils.isEmpty(user)) {
            errors.getErrors().add(new jp.livlog.protopedia.api.share.Error(APIServlet.ERROR_10, APIServlet.ERROR_MAP.get(APIServlet.ERROR_10)));
            this.print(req, res, errors);
            return;
        }

        try {
            // user = StringUtil.deleteCharacters(user, ListServlet.EXCLUSION_CHARACTER);
            final List <ProtoTypeData> all = new ArrayList <>();
            int i = 0;
            while (true) {
                try {
                    final List <ProtoTypeData> list = ProtoTyper.getList(user, i);
                    all.addAll(list);
                } catch (final NullPointerException e) {
                    break;
                }
                i++;
            }

            if (all.isEmpty()) {
                errors.getErrors().add(new jp.livlog.protopedia.api.share.Error(APIServlet.ERROR_01, APIServlet.ERROR_MAP.get(APIServlet.ERROR_01)));
                this.print(req, res, errors);
                return;
            }

            ret.put("prototypes", all);

            this.print(req, res, ret);

        } catch (final Exception e) {
            ListServlet.log.error(e.getMessage(), e);
            errors.getErrors().add(new jp.livlog.protopedia.api.share.Error(APIServlet.ERROR_99, APIServlet.ERROR_MAP.get(APIServlet.ERROR_99)));
            this.print(req, res, errors);
        }

    }
}
