package com.hundsun.boss.common.report;

import java.io.IOException;

import com.lowagie.text.BadElementException;
import com.itextpdf.text.Image;

import org.w3c.dom.Element;
import org.xhtmlrenderer.extend.FSImage;
import org.xhtmlrenderer.extend.ReplacedElement;
import org.xhtmlrenderer.extend.ReplacedElementFactory;
import org.xhtmlrenderer.extend.UserAgentCallback;
import org.xhtmlrenderer.layout.LayoutContext;
import org.xhtmlrenderer.pdf.ITextFSImage;
import org.xhtmlrenderer.pdf.ITextImageElement;
import org.xhtmlrenderer.render.BlockBox;
import org.xhtmlrenderer.simple.extend.FormSubmissionListener;

/**
 * 重写html中的嵌入式图片，使 flyingsauce能够在pdf中输出图片
 */
@SuppressWarnings("restriction")
public class B64ImgReplacedElementFactory implements ReplacedElementFactory {

    public ReplacedElement createReplacedElement(LayoutContext c, BlockBox box, UserAgentCallback uac, int cssWidth, int cssHeight) {
        Element e = box.getElement();
        if (e == null) {
            return null;
        }
        String nodeName = e.getNodeName();
        if (nodeName.equals("img")) {
            String attribute = e.getAttribute("src");
            FSImage fsImage;
            try {
                fsImage = buildImage(attribute, uac);
            } catch (BadElementException e1) {
                fsImage = null;
            } catch (IOException e1) {
                fsImage = null;
            }
            if (fsImage != null) {
                if (cssWidth != -1 || cssHeight != -1) {
                    fsImage.scale(cssWidth, cssHeight);
                }
                return new ITextImageElement(fsImage);
            }
        }
        return null;
    }

    protected FSImage buildImage(String srcAttr, UserAgentCallback uac) throws IOException, BadElementException {
        FSImage fsImage = null;
        if (srcAttr.startsWith("data:image/")) {
            String b64encoded = srcAttr.substring(srcAttr.indexOf("base64,") + "base64,".length(), srcAttr.length());
            byte[] decodedBytes = new sun.misc.BASE64Decoder().decodeBuffer(b64encoded);
            try {
                fsImage = new ITextFSImage(Image.getInstance(decodedBytes));
            } catch (com.itextpdf.text.BadElementException e) {
                e.printStackTrace();
            }
        } else {
            fsImage = uac.getImageResource(srcAttr).getImage();
        }
        return fsImage;
    }

    public void remove(Element e) {
    }

    public void reset() {
    }

    @Override
    public void setFormSubmissionListener(FormSubmissionListener listener) {
    }
}