package com.test.blog.aop;

import com.test.blog.domain.TransactionResult;
import com.test.blog.enumeration.TransactionCode;
import com.test.blog.exception.BadWebClientRequestException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Aspect
@Component
public class TransactionAOP {

    @Pointcut("@annotation(org.springframework.web.bind.annotation.GetMapping)")
    public void GetMapping(){ }

    @Around("GetMapping()")
    public Object Around(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpHeaders headers= new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));

        try {
            return joinPoint.proceed();
        }
        /* 응답전문 파싱에러 */
        catch (ParseException | java.text.ParseException e){
            TransactionCode transactionCode = TransactionCode.findByProcessCode("A0002");
            TransactionResult<Object> transactionResult = new TransactionResult<>(transactionCode.getSuccessFail(),
                    transactionCode.getTransactionCode(),
                    transactionCode.getResultMessage(),
                    null);
            return new ResponseEntity<>(transactionResult, headers, HttpStatus.BAD_GATEWAY);
        }
        /* 정보제공사 4XX에러 처리 */
        catch(BadWebClientRequestException e){
            TransactionCode transactionCode = TransactionCode.findByProcessCode("A0003");
            TransactionResult<Object> transactionResult = new TransactionResult<>(transactionCode.getSuccessFail(),
                    transactionCode.getTransactionCode(),
                    transactionCode.getResultMessage(),
                    null);
            return new ResponseEntity<>(transactionResult, headers, e.getStatusCode());
        }
        /* 알 수 없는 에러 처리 */
        catch (Exception e) {
            TransactionCode transactionCode = TransactionCode.findByProcessCode("A0001");
            TransactionResult<Object> transactionResult = new TransactionResult<>(transactionCode.getSuccessFail(),
                    transactionCode.getTransactionCode(),
                    transactionCode.getResultMessage(),
                    null);
            return new ResponseEntity<>(transactionResult, headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
