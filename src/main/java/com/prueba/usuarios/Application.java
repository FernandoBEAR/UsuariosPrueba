package com.prueba.usuarios;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.prueba.usuarios.entidades.Usuario;
import com.prueba.usuarios.servicios.UsuarioService;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}


    //@Bean
    CommandLineRunner init(UsuarioService usuarioService) {
        return args -> {
            //fbecerraa22_2@unc.edu.pe
            //malvarezt22_1@unc.edu.pe
            //rcastanedac22_1@unc.edu.pe
            Usuario usuario0 = new Usuario("roger fabricio","rmarinr22_2@unc.edu.pe",true);
            Usuario usuario1 = new Usuario("fernando becerra","fbecerraa22_2@unc.edu.pe", true);
            Usuario usuario2 = new Usuario("marcio alvarez","malvarezt22_1@unc.edu.pe", true);
            Usuario usuario3 = new Usuario("paul renzo","rcastanedac22_1@unc.edu.pe", true);

            usuarioService.crearUsuario(usuario0);
            usuarioService.crearUsuario(usuario1);
            usuarioService.crearUsuario(usuario2);
            usuarioService.crearUsuario(usuario3);
        };
    }
}
