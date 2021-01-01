/*
 * Copyright 2021 Root101 (jhernandezb96@gmail.com, +53-5-426-8660).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Or read it directly from LICENCE.txt file at the root of this project.
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.root101.sistema.admin.server;

import com.root101.module.gestion.contabilidad.repo.utils.ResourcesContabilidad;
import com.jhw.module.authorization_server.oauth2.A_ModuleOAuth2;
import com.root101.module.gestion.contabilidad.rest.A_ModuleGestionContabilidadEmpresarial;
import com.root101.module.control.licence.rest.A_ModuleUtilLicence;
import com.jhw.module.util.mysql.services.MySQLHandler;
import com.root101.module.admin.kanban.repo.utils.ResourcesKanban;
import com.root101.module.admin.kanban.rest.A_ModuleAdminKanban;
import com.root101.module.admin.seguridad.repo.utils.ResourcesSeguridad;
import com.root101.module.admin.seguridad.rest.A_ModuleAdminSeguridad;
import com.root101.module.gestion.gastos.repo.utils.ResourcesGastos;
import com.root101.module.gestion.gastos.rest.A_ModuleGestionGastos;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.*;

/**
 *
 * @author Root101 (jhernandezb96@gmail.com, +53-5-426-8660)
 * @author JesusHdezWaterloo@Github
 */
@SpringBootApplication
@ComponentScan(basePackages = {
    A_ModuleGestionContabilidadEmpresarial.BASE_PACKAGE,
    A_ModuleGestionGastos.BASE_PACKAGE,
    A_ModuleAdminKanban.BASE_PACKAGE,
    A_ModuleOAuth2.BASE_PACKAGE,
    A_ModuleAdminSeguridad.BASE_PACKAGE,
    A_ModuleUtilLicence.BASE_PACKAGE,})
@RestController
@RequestMapping(value = "/admin")
public class Application extends SpringBootServletInitializer {

    /**
     * Agregar el nombre de las BD a salvar
     */
    private static void save() {
        MySQLHandler.save(ResourcesContabilidad.SCHEMA);
        MySQLHandler.save(ResourcesGastos.SCHEMA);
        MySQLHandler.save(ResourcesKanban.SCHEMA);
        MySQLHandler.save(ResourcesSeguridad.SCHEMA);
    }

    private static ConfigurableApplicationContext context;

    public static void main(String[] args) {
        context = SpringApplication.run(Application.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(Application.class);
    }

    /**
     * Cierra el servicio, sin seguridad pero bueno
     */
    @GetMapping("/close")
    @CrossOrigin(origins = {"http://127.0.0.1:8080"})//NOT WORKING
    public void close() {
        System.out.println("Cerrando App");
        SpringApplication.exit(context);
        System.out.println("Cerrado contexto");

        System.out.println("Cerrando System(0)");
        System.exit(0);
    }

    @PostConstruct
    public void onCreate() {
        System.out.println("onCreate");
        MySQLHandler.init();
        System.out.println("onCreate finalizado");
    }

    @PreDestroy
    public void onExit() {
        System.out.println("onExit");
        save();
        MySQLHandler.close();
        System.out.println("onExit finalizado");
    }

}
