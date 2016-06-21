package com.bxtpw.common.validation.annotations;

import com.bxtpw.common.validation.validator.ScopeValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * The annotated element size must be between the specified boundaries (included).
 * <p/>
 * Supported types are:
 * <ul>
 * <li>{@code Integer} (length of character sequence is evaluated)</li>
 * </ul>
 * <p/>
 * {@code null} elements are considered valid.
 *
 * @author Emmanuel Bernard
 */
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {ScopeValidator.class})
public @interface Scope {

    String message() default "请输入指定范围内的数字";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * @return size the element must be higher or equal to
     */
    int min() default 0;

    /**
     * @return size the element must be lower or equal to
     */
    int max() default Integer.MAX_VALUE;

    /**
     * 是否允许为空
     */
    boolean allowNull() default true;

    /**
     * Defines several {@link Scope} annotations on the same element.
     *
     * @see Scope
     */
    @Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
    @Retention(RUNTIME)
    @Documented
    @interface List {

        Scope[] value();
    }
}
