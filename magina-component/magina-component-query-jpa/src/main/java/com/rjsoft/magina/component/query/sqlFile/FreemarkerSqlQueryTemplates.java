
package com.rjsoft.magina.component.query.sqlFile;

import com.rjsoft.magina.component.query.jpa.NamedTemplateCallback;
import com.rjsoft.magina.component.query.jpa.NamedTemplateResolver;
import com.rjsoft.magina.component.query.jpa.SftlNamedTemplateResolver;
import com.rjsoft.magina.component.query.jpa.XmlNamedTemplateResolver;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

import java.io.StringWriter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class FreemarkerSqlQueryTemplates implements ResourceLoaderAware, InitializingBean {
    private static final Logger log = LoggerFactory.getLogger(FreemarkerSqlQueryTemplates.class);
    private static Configuration cfg;
    private static StringTemplateLoader sqlTemplateLoader;
    private String encoding = "UTF-8";
    private Map<String, Long> lastModifiedCache = new ConcurrentHashMap();
    private Map<String, Resource> sqlResources = new ConcurrentHashMap();
    private String templateLocation = "classpath*:/sqlQuery";
    private String templateBasePackage = "";
    private ResourceLoader resourceLoader;
    private String suffix = ".dynsql";
    private Map<String, NamedTemplateResolver> suffixResolvers = new HashMap();

    public FreemarkerSqlQueryTemplates() {
        this.suffixResolvers.put(this.suffix, new SftlNamedTemplateResolver());
    }

    public String process(String entityName, String methodName, Map<String, Object> model) {
        this.reloadIfPossible(entityName);

        try {
            StringWriter writer = new StringWriter();
            cfg.getTemplate(this.getTemplateKey(entityName, methodName), this.encoding).process(model, writer);
            String result = writer.toString().trim();
            if (result.endsWith(";")) {
                result = result.substring(0, result.lastIndexOf(";"));
            }

            return result;
        } catch (Exception var6) {
            log.error("process template error. Entity name: " + entityName + " methodName:" + methodName, var6);
            return "";
        }
    }

    private String getTemplateKey(String entityName, String methodName) {
        return entityName + ":" + methodName;
    }

    private void reloadIfPossible(final String entityName) {
        try {
            Long lastModified = (Long) this.lastModifiedCache.get(entityName);
            Resource resource = (Resource) this.sqlResources.get(entityName);
            long newLastModified = resource.lastModified();
            if (lastModified == null || newLastModified > lastModified.longValue()) {
                Iterator iterator = ((NamedTemplateResolver) this.suffixResolvers.get(this.suffix)).doInTemplateResource(resource, new NamedTemplateCallback() {
                    public void process(String templateName, String content) {
                        FreemarkerSqlQueryTemplates.sqlTemplateLoader.putTemplate(FreemarkerSqlQueryTemplates.this.getTemplateKey(entityName, templateName), content);
                    }
                });

                while (iterator.hasNext()) {
                    iterator.next();
                }

                this.lastModifiedCache.put(entityName, newLastModified);
            }
        } catch (Exception var7) {
            log.error("Exception", var7);
        }

    }

    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
        XmlNamedTemplateResolver xmlNamedTemplateResolver = new XmlNamedTemplateResolver(resourceLoader);
        xmlNamedTemplateResolver.setEncoding(this.encoding);
        this.suffixResolvers.put(".xml", xmlNamedTemplateResolver);
    }

    public void afterPropertiesSet() throws Exception {
        Set<String> names = new HashSet();

        for (SqlQueryFileName c : SqlQueryFileName.values()) {
            names.add(c.getFileName());
        }

        String suffixPattern = "/**/*" + this.suffix;
        if (!names.isEmpty()) {
            String pattern;
            if (StringUtils.isNotBlank(this.templateBasePackage)) {
                pattern = "classpath*:" + ClassUtils.convertClassNameToResourcePath(this.templateBasePackage) + suffixPattern;
            } else {
                pattern = this.templateLocation.contains(this.suffix) ? this.templateLocation : this.templateLocation + suffixPattern;
            }

            PathMatchingResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver(this.resourceLoader);
            Resource[] resources = resourcePatternResolver.getResources(pattern);
            Resource[] var7 = resources;
            int var8 = resources.length;

            for (int var9 = 0; var9 < var8; ++var9) {
                Resource resource = var7[var9];
                String resourceName = resource.getFilename().replace(this.suffix, "");
                if (names.contains(resourceName)) {
                    this.sqlResources.put(resourceName, resource);
                }
            }
        }

    }

    public void setTemplateLocation(String templateLocation) {
        this.templateLocation = templateLocation;
    }

    public void setTemplateBasePackage(String templateBasePackage) {
        this.templateBasePackage = templateBasePackage;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }


    static {
        cfg = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
        sqlTemplateLoader = new StringTemplateLoader();
        cfg.setTemplateLoader(sqlTemplateLoader);
    }
}
