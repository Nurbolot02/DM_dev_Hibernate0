package com.ntg;

import com.ntg.entity.Company;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Proxy;

public class ProxyTest {
    @Test
    void dynamicProxyTest() {
        Company company = new Company();
        Proxy.newProxyInstance(company.getClass().getClassLoader(),
                company.getClass().getInterfaces(),
                (o, method, objects) -> method.invoke(objects)
        );

    }
}
