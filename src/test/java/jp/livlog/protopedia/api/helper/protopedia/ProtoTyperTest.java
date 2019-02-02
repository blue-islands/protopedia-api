package jp.livlog.protopedia.api.helper.protopedia;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class ProtoTyperTest {

    @Test
    public void test() {

        try {

            final List <ProtoTypeData> all = new ArrayList <>();
            int i = 0;
            while (true) {
                try {
                    final List <ProtoTypeData> list = ProtoTyper.getList("noby", i);
                    all.addAll(list);
                } catch (final NullPointerException e) {
                    break;
                }
                i++;
            }
            for (final ProtoTypeData data : all) {
                System.out.println(data);
            }

        } catch (final Exception e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
        }
    }

}
