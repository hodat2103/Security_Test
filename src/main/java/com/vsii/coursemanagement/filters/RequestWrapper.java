package com.vsii.coursemanagement.filters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.vsii.coursemanagement.dtos.response.ResponseError;

import com.vsii.coursemanagement.exceptions.XSSServletException;
import com.vsii.coursemanagement.utils.XSSValidationUtils;
import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import org.springframework.http.HttpStatus;
import java.io.*;
import java.util.List;


public class RequestWrapper extends HttpServletRequestWrapper {
    private final String body;

    private List<String> skipWords;

    public RequestWrapper(HttpServletRequest request, List<String> skipWords) throws IOException {
        //So that other request method behave just like before
        super(request);
        this.skipWords = skipWords;

        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = null;
        try {
            InputStream inputStream = request.getInputStream();
            if (inputStream != null) {
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                char[] charBuffer = new char[128];
                int bytesRead = -1;
                while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                    stringBuilder.append(charBuffer, 0, bytesRead);
                }
            } else {
                stringBuilder.append("");
            }
        } catch (IOException ex) {
            throw ex;
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException ex) {
                    throw ex;
                }
            }
        }
        //Store request pody content in 'body' variable
        body = stringBuilder.toString();

    }

    private boolean sanitize(String input) {
        System.out.println("param:" + input);
        if (!XSSValidationUtils.isValidURL(input, skipWords)) {
            ResponseError errorResponse = new ResponseError(HttpStatus.FORBIDDEN.value(),"XSS attack error");

//            errorResponse.setStatus();
//            errorResponse.setMessage();
            try {
                String response = XSSValidationUtils.convertObjectToJson(errorResponse);

                throw new XSSServletException(response);
            } catch (JsonProcessingException e) {
                return false;
            }

        }
        return true;
    }

    // XSS:  Query Param data validation
    @Override
    public String getParameter(String paramName) {
        String value = super.getParameter(paramName);
        sanitize(value);
        return value;
    }

    // XSS:  Query Param data validation
    @Override
    public String[] getParameterValues(String paramName) {
        String values[] = super.getParameterValues(paramName);
        if (null != values) {
            for (int index = 0; index < values.length; index++) {
                sanitize(values[index]);
            }
        }
        return values;
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(body.getBytes());
        ServletInputStream servletInputStream = new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {

            }

            public int read() throws IOException {
                return byteArrayInputStream.read();
            }
        };
        return servletInputStream;
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(this.getInputStream()));
    }

    //Use this method to read the request body N times
    public String getBody() {
        return this.body;
    }
}