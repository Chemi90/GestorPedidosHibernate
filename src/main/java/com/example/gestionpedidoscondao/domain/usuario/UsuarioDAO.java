package com.example.gestionpedidoscondao.domain.usuario;

import com.example.gestionpedidoscondao.domain.DAO;
import com.example.gestionpedidoscondao.domain.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase UsuarioDAO.
 * Implementa la interfaz DAO para la gestión de objetos de tipo Usuario en la base de datos.
 * Permite realizar operaciones como buscar, guardar, actualizar y eliminar usuarios.
 *
 * @author Author Name
 * @version 1.0
 * @since 2023-11-21
 */
public class UsuarioDAO implements DAO {

    /**
     * Método no implementado para obtener todos los usuarios.
     *
     * @return ArrayList<Usuario> Retorna null ya que el método no está implementado.
     * @deprecated Este método no está implementado. Su uso no es recomendado.
     */
    @Override
    public ArrayList<Usuario> getAll() {return null;}

    /**
     * Método no implementado para obtener un Usuario por su ID.
     *
     * @param id El ID único del Usuario a obtener.
     * @return Usuario Retorna null ya que el método no está implementado.
     * @deprecated Este método no está implementado. Su uso no es recomendado.
     */
    @Override
    public Usuario get(Long id) {return null;}

    /**
     * Guarda un objeto Usuario en la base de datos.
     * Abre una sesión con Hibernate, inicia una transacción, guarda el usuario,
     * y maneja las transacciones y excepciones correspondientes.
     *
     * @param data El objeto Usuario a ser guardado.
     * @return Usuario El objeto Usuario guardado, o null en caso de error.
     */
    @Override
    public Usuario save(Object data) {
        Usuario usuario = (Usuario) data;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.save(usuario);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return usuario;
    }

    /**
     * Método no implementado para actualizar un Usuario.
     *
     * @param data El objeto Usuario a actualizar.
     * @deprecated Este método no está implementado. Su uso no es recomendado.
     */
    @Override
    public void update(Object data) {}

    /**
     * Método no implementado para eliminar un Usuario.
     *
     * @param data El objeto Usuario a eliminar.
     * @deprecated Este método no está implementado. Su uso no es recomendado.
     */
    @Override
    public void delete(Object data) {}

    /**
     * Método no implementado para eliminar un Usuario.
     *
     * @param o El objeto Usuario a eliminar.
     * @return boolean Siempre retorna falso ya que el método no está implementado.
     * @deprecated Este método no está implementado. Su uso no es recomendado.
     */
    @Override
    public boolean remove(Object o) {
        return false;
    }

    /**
     * Valida las credenciales de un usuario.
     * Realiza una consulta en la base de datos para verificar si existe un usuario con el nombre de usuario y contraseña proporcionados.
     *
     * @param username Nombre de usuario a validar.
     * @param password Contraseña a validar.
     * @return Usuario El usuario validado, o null si las credenciales no son válidas o si ocurre un error.
     */
    public Usuario validateUser(String username, String password){
        Usuario result = null;
        try( Session session = HibernateUtil.getSessionFactory().openSession()){
            Query<Usuario> q = session.createQuery("from Usuario where nombre=:u and contraseña=:p",Usuario.class);
            q.setParameter("u",username);
            q.setParameter("p",password);

            try {
                result = q.getSingleResult();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        return result;
    }
}