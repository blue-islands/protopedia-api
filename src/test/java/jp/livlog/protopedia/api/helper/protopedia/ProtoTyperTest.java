package jp.livlog.protopedia.api.helper.protopedia;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class ProtoTyperTest {

    @Test
    public void testDetail() {

        ProtoTypeDetailData data = null;
        try {
            data = ProtoTyper.getDetail("c88d8d0a6097754525e02c2246d8d27f");
            System.out.println(data);
            // data = ProtoTyper.getDetail("38913e1d6a7b94cb0f55994f679f5956");
            // System.out.println(data);
        } catch (final Exception e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
        }
    }


    @Test
    public void testList() {

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


    @Test
    public void testTimestamp() {

        // String date = null;
        // try {
        // date = ProtoTyper.getTimestamp("先端発見!", "c88d8d0a6097754525e02c2246d8d27f");
        // System.out.println(date);
        // } catch (final Exception e) {
        // // TODO 自動生成された catch ブロック
        // e.printStackTrace();
        // }
    }
}
