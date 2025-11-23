/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.proyetogrupo.proyetofinal;

import com.proyetogrupo.proyetofinal.persistencia.DB;

/**
 *
 * @author ibner
 */
public class ProyetoFinal {

    public static void main(String[] args) {
        System.out.println("Hello World!");
        System.out.println("ok? okaaayyyy!!!");

        // Isso garante que o banco existe antes de abrir qualquer tela
        try {
            DB.inicializarBanco();
        } catch (Exception e) {
            System.out.println("Erro crítico ao criar banco: " + e.getMessage());
            // Encerra o programa se não tiver banco 
        } 
    }
}
