package cn.longicorn.modules.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 使用该annotaion表示当前属性或方法只能够在持有对象内置锁(synchronization)或显式的
 * java.util.concurrent.Lock锁实例的时候才可以调用。
 *
 * 参数表示了是哪个lock在保护被标注的属性或方法
 * <ul>
 * <li>
 * <code>this</code> : 对象内置锁
 * </li>
 * <li>
 * <code>class-name.this</code> : 嵌套类中使用，避免this出现歧义
 * </li>
 * <li>
 * <code>itself</code> : 仅在引用字段中用，表示该字段引用的对象
 * </li>
 * <li>
 * <code>field-name</code> : <em>field-name</em>引用的锁对象
 * </li>
 * <li>
 * <code>class-name.field-name</code> : <em>class-name.field-name</em>静态属性引用的锁对象
 * </li>
 * <li>
 * <code>method-name()</code> : 方法返回的锁对象
 * </li>
 * <li>
 * <code>class-name.class</code> : 类对象锁
 * </li>
 */
@Documented
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.CLASS) // The original version used RUNTIME
public @interface GuardedBy {
    String value();
}
