package com.github.jusm.component;
import java.util.Map;  
  
import org.springframework.beans.MutablePropertyValues;  
import org.springframework.beans.factory.support.DefaultListableBeanFactory;  
import org.springframework.beans.factory.support.GenericBeanDefinition;  
import org.springframework.context.ApplicationContext;  
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;  
  
@Component
public class SpringContextHolder implements ApplicationContextAware {  
      
    private static ApplicationContext applicationContext;  
      
    /** 
     * 实现ApplicationContextAware接口的context注入函数, 将其存入静态变量. 
     */  
    public void setApplicationContext(ApplicationContext applicationContext) {  
        SpringContextHolder.applicationContext =applicationContext;  
    }  
      
    /** 
     * 取得存储在静态变量中的ApplicationContext. 
     */  
    public static ApplicationContext getApplicationContext() {  
        checkApplicationContext();  
        return applicationContext;  
    }  
      
    /** 
     * 从静态变量ApplicationContext中取得Bean, 自动转型为所赋值对象的类型. 
     */  
    @SuppressWarnings("unchecked")  
    public static <T> T getBean(String name) {  
        checkApplicationContext();  
        if (applicationContext.containsBean(name)) {  
            return (T) applicationContext.getBean(name);  
        }  
        return null;  
    }  
  
    /** 
     * 从静态变量ApplicationContext中取得Bean, 自动转型为所赋值对象的类型. 
     */  
    public static <T> Map<String,T> getBeansOfType(Class<T> clazz) {  
        checkApplicationContext();  
        return  applicationContext.getBeansOfType(clazz);  
    }  
    
    public static <T> T getBean(Class<T> clazz) {  
        checkApplicationContext();  
        return  applicationContext.getBean(clazz);  
    }  
  
    private static void checkApplicationContext() {  
        if (applicationContext == null)  
            throw new IllegalStateException("applicaitonContext未注入,请在applicationContext.xml中定义SpringContextHolder");  
    }  
      
    /** 
     * 同步方法注册bean到ApplicationContext中 
     *  
     * @param beanName 
     * @param clazz 
     * @param original bean的属性值 
     */  
    public static synchronized void setBean(String beanName, Class<?> clazz,Map<String,Object> original) {  
        checkApplicationContext();  
        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) applicationContext.getAutowireCapableBeanFactory();  
        if(beanFactory.containsBean(beanName)){  
            return;  
        }  
        //BeanDefinition beanDefinition = new RootBeanDefinition(clazz);  
        GenericBeanDefinition definition = new GenericBeanDefinition();  
        //类class  
        definition.setBeanClass(clazz);  
        //属性赋值  
        definition.setPropertyValues(new MutablePropertyValues(original));  
        //注册到spring上下文  
        beanFactory.registerBeanDefinition(beanName, definition);  
    }  
      
    /** 
     * 删除spring中管理的bean 
     * @param beanName 
     */  
    public static void removeBean(String beanName){  
        ApplicationContext ctx = SpringContextHolder.getApplicationContext();  
        DefaultListableBeanFactory acf = (DefaultListableBeanFactory) ctx.getAutowireCapableBeanFactory();  
        if(acf.containsBean(beanName)) {  
            acf.removeBeanDefinition(beanName);  
        }  
    }  
}  