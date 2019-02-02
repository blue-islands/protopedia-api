package jp.livlog.protopedia.api.share;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * URLのパラメータ設定をする.
 *
 * @author H.Aoshima
 * @version 1.0
 */
public class Parameters implements Serializable {

    /**
     * シリアルバージョンUID.
     */
    private static final long serialVersionUID = 1L;

    /** parameters. */
    private List <Parameter>  parameters       = new ArrayList <>();


    /**
     * @param name String
     * @param value Object
     */
    public final void addParameter(final String name, final Object value) {

        this.parameters.add(new Parameter(name, String.valueOf(value)));
    }


    /**
     * @return parameterList
     */
    public final List <Parameter> getParameters() {

        return this.parameters;
    }


    /**
     * @param pParameters セットする parameters
     */
    public final void setParameters(final List <Parameter> pParameters) {

        this.parameters = pParameters;
    }

    /**
     * パラメータを保持する.
     *
     * @author H.Aoshima
     * @version 1.0
     */
    class Parameter implements Serializable {

        /**
         * シリアルバージョンUID.
         */
        private static final long serialVersionUID = 1L;

        /** name. */
        private String            name;

        /** value. */
        private String            value;


        /**
         * コンストラクタ.
         * @param pName String
         * @param pValue String
         */
        Parameter(final String pName, final String pValue) {

            this.name = pName;
            this.value = pValue;
        }


        /**
         * @return name
         */
        public String getName() {

            return this.name;
        }


        /**
         * @param pName セットする name
         */
        public void setName(final String pName) {

            this.name = pName;
        }


        /**
         * @return value
         */
        public String getValue() {

            return this.value;
        }


        /**
         * @param pValue セットする value
         */
        public void setValue(final String pValue) {

            this.value = pValue;
        }

    }


    /*
     * (非 Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public final String toString() {

        final StringBuffer sb = new StringBuffer();

        boolean first = true;
        for (final Parameter param : this.parameters) {
            if (first) {
                sb.append(param.getName() + "=" + param.getValue());
                first = false;
            } else {
                sb.append("&" + param.getName() + "=" + param.getValue());
            }
        }
        return sb.toString();
    }
}
