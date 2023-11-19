package com.example.gestionpedidoscondao.domain.usuario;

import com.example.gestionpedidoscondao.domain.DAO;
import com.example.gestionpedidoscondao.domain.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO implements DAO {

    @Override
    public ArrayList<Usuario> getAll() {
        ArrayList<Usuario> usuarios = new ArrayList<>();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Usuario> query = session.createQuery("from Usuario", Usuario.class);
            usuarios = (ArrayList<Usuario>) query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return usuarios;
    }

    @Override
    public Usuario get(Long id) {
        Usuario usuario = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            usuario = session.get(Usuario.class, id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return usuario;
    }

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

    @Override
    public void update(Object data) {
        Usuario usuario = (Usuario) data;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.update(usuario);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Object data) {
        Usuario usuario = (Usuario) data;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.delete(usuario);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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
