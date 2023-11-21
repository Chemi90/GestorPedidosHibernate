package com.example.gestionpedidoscondao.domain.producto;

import com.example.gestionpedidoscondao.domain.DAO;
import com.example.gestionpedidoscondao.domain.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import java.util.ArrayList;


/**
 * Clase ProductoDAO
 * Implementa la interfaz DAO para la gestión de objetos de tipo Producto en la base de datos.
 * Proporciona operaciones CRUD (Crear, Leer, Actualizar, Eliminar) para objetos Producto.
 *
 * @author José Miguel Ruiz Guevara
 * @version 1.0
 * @since 2023-11-21
 */
public class ProductoDAO implements DAO {

    /**
     * Obtiene todos los productos de la base de datos.
     * Abre una sesión con Hibernate, ejecuta una consulta para obtener todos los productos,
     * y maneja las transacciones y excepciones correspondientes.
     *
     * @return ArrayList<Producto> Una lista de todos los objetos Producto.
     *         Retorna una lista vacía si ocurre un error.
     */
    @Override
    public ArrayList<Producto> getAll() {
        Session session = null;
        Transaction tx = null;
        ArrayList<Producto> productos = new ArrayList<>();

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();

            Query<Producto> query = session.createQuery("FROM Producto", Producto.class);
            productos = new ArrayList<>(query.list());

            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }

        return productos;
    }

    /**
     * Método no implementado para obtener un Producto por su ID.
     *
     * @param id El ID único del Producto a obtener.
     * @return Producto Retorna null ya que el método no está implementado.
     * @deprecated Este método no está implementado. Su uso no es recomendado.
     */
    @Override
    public Producto get(Long id) {return null;}

    /**
     * Guarda un objeto Producto en la base de datos.
     * Abre una sesión con Hibernate, inicia una transacción, guarda el producto,
     * y maneja las transacciones y excepciones correspondientes.
     *
     * @param data El objeto Producto a ser guardado.
     * @return Producto El objeto Producto guardado, o null en caso de error.
     */
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

    /**
     * Método no implementado para actualizar un Producto.
     *
     * @param data El objeto Producto a actualizar.
     * @deprecated Este método no está implementado. Su uso no es recomendado.
     */
    @Override
    public void update(Object data) {}

    /**
     * Método no implementado para eliminar un Producto.
     *
     * @param data El objeto Producto a eliminar.
     * @deprecated Este método no está implementado. Su uso no es recomendado.
     */
    @Override
    public void delete(Object data) {}

    /**
     * Método no implementado para eliminar un Producto.
     *
     * @param o El objeto Producto a eliminar.
     * @return boolean Siempre retorna falso ya que el método no está implementado.
     * @deprecated Este método no está implementado. Su uso no es recomendado.
     */
    @Override
    public boolean remove(Object o) {
        return false;
    }

    /**
     * Busca un producto por su nombre en la base de datos.
     * Abre una sesión con Hibernate, ejecuta una consulta para buscar el producto por nombre,
     * y maneja las excepciones correspondientes.
     *
     * @param nombre El nombre del producto a buscar.
     * @return Producto El objeto Producto encontrado, o null si no se encuentra o si ocurre un error.
     */
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