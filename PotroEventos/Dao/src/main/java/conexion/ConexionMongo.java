package conexion;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import entidadesmongo.AsientoEventoMongoEntidad;
import entidadesmongo.AsientoMongoEntidad;
import entidadesmongo.BoletoMongoEntidad;
import entidadesmongo.CategoriaMongoEntidad;
import entidadesmongo.EventoMongoEntidad;
import entidadesmongo.ReservacionMongoEntidad;
import entidadesmongo.SeccionMongoEntidad;
import entidadesmongo.UbicacionMongoEntidad;
import entidadesmongo.UsuarioMongoEntidad;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

/**
 *
 * @author Kaleb
 */
public class ConexionMongo {

    private static final String URL = "mongodb://localhost:27017";

    private static final String BASE_DATOS = "potro_eventos";

    public static MongoClient cliente;

    public static final MongoClient obtenerCliente() {
        if (cliente == null) {
            //Proovedor pojos
            CodecProvider proovedorPojo = PojoCodecProvider.builder()
                    .automatic(true)
                    .build();

            //almacen codecs
            CodecRegistry registroCodecs = CodecRegistries.fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), CodecRegistries.fromProviders(proovedorPojo));

            MongoClientSettings configuracionMongo = MongoClientSettings.builder()
                    .applyConnectionString(new ConnectionString(URL))
                    .codecRegistry(registroCodecs)
                    .build();

            cliente = MongoClients.create(configuracionMongo);
        }

        return cliente;
    }

    public static MongoDatabase obtenerBaseDatos() {
        return obtenerCliente().getDatabase(BASE_DATOS);
    }
    
    public static MongoCollection<AsientoMongoEntidad> obtenerColeccionAsientos() {
        return obtenerBaseDatos()
                .getCollection("asientos", AsientoMongoEntidad.class);
    }

    public static MongoCollection<EventoMongoEntidad> obtenerColeccionEventos() {
        return obtenerBaseDatos()
                .getCollection("eventos", EventoMongoEntidad.class);
    }
    
    public static MongoCollection<UsuarioMongoEntidad> obtenerColeccionUsuarios() {
        return obtenerBaseDatos()
                .getCollection("usuarios", UsuarioMongoEntidad.class);
    }
    
    public static MongoCollection<UbicacionMongoEntidad> obtenerColeccionUbicaciones() {
        return obtenerBaseDatos()
                .getCollection("ubicaciones", UbicacionMongoEntidad.class);
    }
    
    public static MongoCollection<CategoriaMongoEntidad> obtenerColeccionCategorias() {
        return obtenerBaseDatos()
                .getCollection("categorias", CategoriaMongoEntidad.class);
    }
    
    public static MongoCollection<SeccionMongoEntidad> obtenerColeccionSecciones() {
        return obtenerBaseDatos()
                .getCollection("secciones", SeccionMongoEntidad.class);
    }
    
    public static MongoCollection<ReservacionMongoEntidad> obtenerColeccionReservaciones() {
        return obtenerBaseDatos()
                .getCollection("reservaciones", ReservacionMongoEntidad.class);
    }
    
    public static MongoCollection<AsientoEventoMongoEntidad> obtenerColeccionAsientosEvento() {
        return obtenerBaseDatos()
                .getCollection("asientoEventos", AsientoEventoMongoEntidad.class);
    }
    
    public static MongoCollection<UbicacionMongoEntidad> obtenerColeccionUbicacionea(){
        return obtenerBaseDatos()
                .getCollection("ubicaciones", UbicacionMongoEntidad.class);
    }
    
    public static MongoCollection<BoletoMongoEntidad> obtenerColeccionBoletos(){
        return obtenerBaseDatos().getCollection("boletos", BoletoMongoEntidad.class);
    }
}
