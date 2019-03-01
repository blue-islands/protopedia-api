/*
 * タイトル：共通モジュールプロジェクト.
 * 説明    ：
 * 著作権  ：Copyright(c) 2017 livlog llc.
 * 会社名  ：リブログ合同会社
 * 変更履歴：20xx.xx.xx
 *         ：新規登録
 */
package jp.livlog.protopedia.api.share;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * 文章編集のユーティリティ.
 *
 * @author H.Aoshima
 * @version 1.0
 *
 */
public final class BreakUtil {

    /**
     * コンストラクタ.
     */
    private BreakUtil() {

    }


    /**
     * 文章を行単位変更します.
     * @param source 文章
     * @return 行単位
     */
    public static List <String> convSentenceToRow(final String source) {

        final List <String> ret = new ArrayList <>();

        final BreakIterator boundary = BreakIterator.getSentenceInstance(Locale.US);
        boundary.setText(source);

        int start = boundary.first();
        for (int end = boundary.next(); end != BreakIterator.DONE; start = end, end = boundary.next()) {
            ret.add(source.substring(start, end));
        }
        return ret;
    }

}
