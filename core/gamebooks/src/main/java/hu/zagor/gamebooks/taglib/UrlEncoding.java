package hu.zagor.gamebooks.taglib;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * Tag class that allows values to be url encoded in jsp.
 * @author Tamas_Szekeres
 *
 */
public class UrlEncoding extends TagSupport {
    private String var;
    private String value;

    @Override
    public int doStartTag() throws JspException {
        try {
            if (var != null) {
                pageContext.setAttribute(var, acquireString(), PageContext.PAGE_SCOPE);
            } else {
                pageContext.getOut().print(acquireString());
            }
        } catch (final Exception exception) {
            exception.getClass();
        }
        return EVAL_PAGE;
    }

    private String acquireString() throws UnsupportedEncodingException {
        return URLEncoder.encode(value, "UTF-8");
    }

    public String getVar() {
        return var;
    }

    public void setVar(final String var) {
        this.var = var;
    }

    public String getValue() {
        return value;
    }

    public void setValue(final String value) {
        this.value = value;
    }

}
