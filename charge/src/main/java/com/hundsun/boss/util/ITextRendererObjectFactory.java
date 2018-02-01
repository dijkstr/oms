package com.hundsun.boss.util;

import java.io.IOException;

import org.apache.commons.pool.BasePoolableObjectFactory;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.hundsun.boss.common.Constant;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.BaseFont;

/**
 * ITextRenderer对象工厂,提供性能,加载中文字体集(大小20M),故增加对象池
 */
public class ITextRendererObjectFactory extends BasePoolableObjectFactory {
    private static GenericObjectPool itextRendererObjectPool = null;

    @Override
    public Object makeObject() throws Exception {
        ITextRenderer renderer = createTextRenderer();
        return renderer;
    }

    /**
     * 获取对象池,使用对象工厂 后提供性能,能够支持 500线程 迭代10
     * 
     * @return
     */
    public static GenericObjectPool getObjectPool() {
        synchronized (ITextRendererObjectFactory.class) {
            if (itextRendererObjectPool == null) {
                itextRendererObjectPool = new GenericObjectPool(new ITextRendererObjectFactory());
                GenericObjectPool.Config config = new GenericObjectPool.Config();
                config.lifo = false;
                config.maxActive = 15;
                config.maxIdle = 5;
                config.minIdle = 1;
                config.maxWait = 5 * 1000;
                itextRendererObjectPool.setConfig(config);
            }
        }

        return itextRendererObjectPool;
    }

    /**
     * 初始化
     * 
     * @return
     * @throws Exception
     */
    public static synchronized ITextRenderer createTextRenderer() throws Exception {
        ITextRenderer renderer = new ITextRenderer();
        ITextFontResolver fontResolver = renderer.getFontResolver();
        addFonts(fontResolver);
        return renderer;
    }

    /**
     * 添加字体
     * 
     * @param fontResolver
     * @return
     * @throws IOException 
     * @throws DocumentException
     */
    public static ITextFontResolver addFonts(ITextFontResolver fontResolver) throws DocumentException, IOException {
        // 加载微软雅黑字体
        fontResolver.addFont(Constant.pdf_font_file_path, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
//        fontResolver.addFont(PropertyUtil.readProperty("/", "reportsetting.properties", "fontfilepath"), BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
        return fontResolver;
    }
}
