package com.sun.content.template;

import com.sun.content.api.entity.EsSyncable;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Deprecated
public class ContentQueryTemplateFactory implements ApplicationListener<ContextRefreshedEvent> {

    private Map<Class<EsSyncable>, AbstractContentQueryTemplate<EsSyncable>> templateMap = new HashMap<>();

    private Map<String, AbstractContentQueryTemplate<EsSyncable>> templateNameMap = new HashMap<>();

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        ApplicationContext context = event.getApplicationContext();
        Map<String, AbstractContentQueryTemplate> beans = context.getBeansOfType(AbstractContentQueryTemplate.class);

        for (AbstractContentQueryTemplate b : beans.values()) {
            Class type = b.getType();
            templateMap.put(type, b);
        }
    }

    public Map<Class<EsSyncable>, AbstractContentQueryTemplate<EsSyncable>> getTemplateMap() {
        return this.templateMap;
    }

}
