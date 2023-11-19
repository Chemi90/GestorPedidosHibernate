package com.example.gestionpedidoscondao.domain.producto;

import com.example.gestionpedidoscondao.domain.DAO;
import com.example.gestionpedidoscondao.domain.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;
import java.util.ArrayList;


/**
 * Implementación de la interfaz ProductoDAO para realizar operaciones CRUD
 * sobre la entidad Producto en la base de datos.
 *
 * Utiliza consultas SQL para interactuar con la base de datos y mapea los
 * resultados a objetos de tipo Producto.
 *
 * @author José Miguel Ruiz Guevara
 * @version 1.0
 * @since 1.0
 */
public class ProductoDAO implements DAO {


    @Override
    public ArrayList<Producto> getAll() {
        ArrayList<Producto> productos = new ArrayList<>();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Producto> query = session.createQuery("from Producto", Producto.class);
            productos = (ArrayList<Producto>) query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return productos;
    }


    @Override
    public Producto get(Long id) {
        Producto producto = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            producto = session.get(Producto.class, id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return producto;
    }


    @Override
    public Producto save(Object data) {
        Producto producto = (Producto) data;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.save(producto);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return producto;
    }


    @Override
    public void update(Object data) {
        Producto producto = (Producto) data;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.update(producto);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void delete(Object data) {
        Producto producto = (Producto) data;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.delete(producto);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

        public Producto findByName(String nombre) {
            Producto producto = null;
            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                Query<Producto> query = session.createQuery("FROM Producto WHERE nombre = :nombre", Producto.class);
                query.setParameter("nombre", nombre);
                producto = query.uniqueResult();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return producto;
        }
    }



