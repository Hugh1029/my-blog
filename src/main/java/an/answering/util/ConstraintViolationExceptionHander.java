package an.answering.util;


import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

/**
 * 异常处理器
 * Created by HP on 2017/8/18.
 */
public class ConstraintViolationExceptionHander {

    public static String getMessage(ConstraintViolationException e){
        List<String> msgList = new ArrayList<>();
        for (ConstraintViolation<?> constraintViolation : e.getConstraintViolations()){
            msgList.add(constraintViolation.getMessage());
        }

        String messages = StringUtils.join(msgList.toArray(), ';');
        return messages;
    }
}
