/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.oozie.servlet;

import org.apache.oozie.service.Services;

import javax.servlet.ServletContextListener;
import javax.servlet.ServletContextEvent;

/**
 * Webapp context listener that initializes Oozie {@link Services}.
 */

/**
 * 在 Servlet API 中有一个 ServletContextListener 接口，它能够监听 ServletContext 对象的生命周期，
 * 实际上就是监听 Web 应用的生命周期。
 *
 * 当Servlet 容器启动或终止Web 应用时，会触发ServletContextEvent 事件，
 * 该事件由ServletContextListener 来处理。在 ServletContextListener
 * 接口中定义了处理ServletContextEvent 事件的两个方法。
 */
public class ServicesLoader implements ServletContextListener {
    private static Services services;
    private static boolean sslEnabled = false;

    /**
     * Initialize Oozie services.
     *
     * @param event context event.
     */
    /**
     * 当Servlet 容器启动Web 应用时调用该方法。在调用完该方法之后，容器再对Filter 初始化，
     * 并且对那些在 Web 应用启动时就需要被初始化的Servlet 进行初始化。
     *
     * @param event
     */
    public void contextInitialized(ServletContextEvent event) {
        try {
            String ssl = event.getServletContext().getInitParameter("ssl.enabled");
            if (ssl != null) {
                sslEnabled = true;
            }

            services = new Services();
            services.init();
        }
        catch (Throwable ex) {
            System.out.println();
            System.out.println("ERROR: Oozie could not be started");
            System.out.println();
            System.out.println("REASON: " + ex.toString());
            System.out.println();
            System.out.println("Stacktrace:");
            System.out.println("-----------------------------------------------------------------");
            ex.printStackTrace(System.out);
            System.out.println("-----------------------------------------------------------------");
            System.out.println();
            System.exit(1);
        }
    }

    /**
     * Destroy Oozie services.
     *
     * @param event context event.
     */
    public void contextDestroyed(ServletContextEvent event) {
        services.destroy();
    }

    public static boolean isSSLEnabled() {
        return sslEnabled;
    }
}
