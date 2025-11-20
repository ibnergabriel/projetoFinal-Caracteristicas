/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.proyetogrupo.proyetofinal.persistencia;

/**
 *
 * @author ibner
 */
public class DBIntegrityException extends RuntimeException {
    
    public DBIntegrityException(String msg){
        super(msg);
    }
    
}
