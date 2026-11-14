package com.example.habitar.features.autoconocimiento

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.habitar.data.RespuestasCatq
import com.example.habitar.data.RespuestasRbq2a
import com.example.habitar.data.ScreeningApiService
import com.example.habitar.data.ScreeningRequest
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// Modelos del perfil dinámico de resultados
data class SubescalaResultado(
    val titulo: String,
    val nivelTexto: String, // "Baja", "Moderada", "Alta"
    val nivelColorHex: Long, // 0xFF798E9C, 0xFFD99B43, 0xFF5E5A80
    val progreso: Float,    // 0.15f a 0.95f
    val descripcion: String
)

data class PerfilOrientacion(
    val esfuerzoSocial: SubescalaResultado,
    val cuidarMuestras: SubescalaResultado,
    val preferenciaPredecible: SubescalaResultado,
    val movimientosRepetidos: SubescalaResultado
)

class AutoconocimientoViewModel(application: Application) : AndroidViewModel(application) {

    private val prefs: SharedPreferences = application.getSharedPreferences(
        "habitar_screening_prefs",
        Context.MODE_PRIVATE
    )

    val respuestasCatq = mutableStateMapOf<Int, Int>()
    val respuestasRbq2a = mutableStateMapOf<Int, Int>()

    var mostrarAyudaBottomSheet by mutableStateOf(false)
        private set

    var perfilResultado by mutableStateOf<PerfilOrientacion?>(null)
        private set

    init {
        cargarProgreso()
        if (respuestasCatq.isNotEmpty() || respuestasRbq2a.isNotEmpty()) {
            perfilResultado = calcularPerfilReal()
        }
    }

    val preguntasCatq = listOf(
        "Cuando estoy interactuando con alguien, pienso deliberadamente en mi lenguaje corporal o mis expresiones faciales.",
        "Monitoreo mi lenguaje corporal o mis expresiones faciales para que parezcan naturales.",
        "Rara vez siento la necesidad de fingir para caer bien.",
        "Tengo que pensar en qué expresiones faciales debería poner.",
        "Ajusto mi lenguaje corporal o mi tono de voz para agradar a la otra persona.",
        "Practico mis expresiones faciales o mi postura para parecer más seguro o accesible.",
        "Sé cuándo es mi turno de hablar en una conversación.",
        "Tengo que obligarme a hacer contacto visual con la gente.",
        "Trato de controlar mis emociones para no incomodar a los demás.",
        "Cuando estoy con otras personas, me siento como si estuviera interpretando un papel o actuando.",
        "Tengo que recordarme a mí mismo que debo sonreír cuando alguien me cuenta algo agradable.",
        "Me resulta fácil y natural mantener una conversación fluida.",
        "Copio el comportamiento, los gestos o las frases de otras personas para encajar.",
        "Me esfuerzo conscientemente por no hablar demasiado sobre mis propios intereses.",
        "Pienso de antemano sobre los temas de conversación que podría tocar antes de una reunión social.",
        "Suelo analizar las situaciones sociales después de que ocurren para ver si me comporté de manera adecuada.",
        "Intento ocultar mis rarezas o peculiaridades a los demás.",
        "Uso frases ensayadas o guiones mentales cuando hablo con personas.",
        "En situaciones sociales me siento relajado y espontáneo sin tener que pensar en cómo actúo.",
        "Miro cómo actúan los demás para saber cómo debo reaccionar yo.",
        "Finjo interés en lo que la otra persona está diciendo solo para mantener la armonía.",
        "Me cuesta muy poco trabajo relacionarme con personas nuevas.",
        "Me siento agotado física y mentalmente después de pasar tiempo socializando.",
        "Puedo ser completamente yo mismo cuando estoy con otras personas.",
        "Hago un gran esfuerzo por ocultar mi malestar o incomodidad en lugares concurridos o ruidosos."
    )

    val preguntasRbq2a = listOf(
        "¿Te cuesta cambiar de una actividad a otra a menos que hayas sido avisado con anticipación?",
        "¿Ordenas o alineas objetos de una manera muy específica o con un patrón estricto?",
        "¿Repites ciertos movimientos con las manos o el cuerpo (como aletear, mecerte o tronarte los dedos) cuando estás concentrado o emocionado?",
        "¿Prefieres comer siempre la misma comida o seguir exactamente el mismo menú cada día?",
        "¿Hay temas, materias o pasatiempos específicos que te apasionan tanto que puedes pasar horas investigando sobre ellos?",
        "¿Te incomoda o angustia si cambian imprevistamente los horarios de clases, reuniones o actividades cotidianas?",
        "¿Sueles seguir exactamente la misma ruta o camino para ir a la universidad, trabajo o lugares habituales?",
        "¿Te gusta tocar, oler o explorar texturas de objetos de una forma particular y repetitiva?",
        "¿Tienes rituales o pasos obligatorios antes de empezar a estudiar, trabajar o ir a dormir?",
        "¿Te cuesta tolerar que muevan tus pertenencias o cambien el orden de tu espacio personal?",
        "¿Haces preguntas sobre el mismo tema una y otra vez, incluso si ya conoces la respuesta?",
        "¿Te molestan intensamente ruidos cotidianos específicos (como ruidos de masticar, luces parpadeantes o murmullos)?",
        "¿Prefieres que las conversaciones se enfoquen principalmente en tus áreas de interés?",
        "¿Revisas cosas repetidamente (puertas, cerrojos, mochilas, alarmas) más de lo necesario?",
        "¿Te resulta difícil detener una actividad en la que estás inmerso hasta que sientes que quedó 'perfecta'?",
        "¿Coleccionas o acumulas objetos específicos que para otras personas podrían parecer poco útiles?",
        "¿Te sientes incómodo o irritado si alguien interrumpe una rutina que tenías planificada para tu día?",
        "¿Sueles notar detalles minuciosos del entorno que la mayoría de la gente pasa por alto?",
        "¿Prefieres actividades predecibles y estructuradas por sobre planes espontáneos o improvisados?",
        "¿Tienes frases, citas de series o sonidos que repites mentalmente o en voz alta con frecuencia?"
    )

    fun guardarRespuesta(idSeccion: Int, idPregunta: Int, valor: Int) {
        val editor = prefs.edit()
        if (idSeccion == 1) {
            respuestasCatq[idPregunta] = valor
            editor.putInt("catq_p_$idPregunta", valor)
        } else if (idSeccion == 2) {
            respuestasRbq2a[idPregunta] = valor
            editor.putInt("rbq2a_p_$idPregunta", valor)
        }
        editor.putBoolean("tiene_progreso_guardado", true)
        editor.apply()
    }

    fun obtenerRespuestaActual(idSeccion: Int, idPregunta: Int): Int? {
        return if (idSeccion == 1) respuestasCatq[idPregunta] else respuestasRbq2a[idPregunta]
    }

    fun obtenerPrimerIndiceSinResponder(idSeccion: Int): Int {
        val total = obtenerTotalPreguntas(idSeccion)
        val respuestas = obtenerMapaRespuestas(idSeccion)
        for (i in 1..total) {
            if (!respuestas.containsKey(i)) {
                return i - 1
            }
        }
        return 0
    }

    fun marcarSeccionCompletada(idSeccion: Int) {
        prefs.edit().putBoolean("seccion_${idSeccion}_completada", true).apply()
    }

    fun estaSeccionCompletada(idSeccion: Int): Boolean {
        return prefs.getBoolean("seccion_${idSeccion}_completada", false)
    }

    fun todasLasSeccionesCompletadas(): Boolean {
        return estaSeccionCompletada(1) && estaSeccionCompletada(2)
    }

    fun obtenerCantidadRespondidas(idSeccion: Int): Int {
        return if (idSeccion == 1) respuestasCatq.size else respuestasRbq2a.size
    }

    fun obtenerTotalPreguntas(idSeccion: Int): Int {
        return if (idSeccion == 1) preguntasCatq.size else preguntasRbq2a.size
    }

    fun obtenerPreguntas(idSeccion: Int): List<String> {
        return if (idSeccion == 1) preguntasCatq else preguntasRbq2a
    }

    fun obtenerMapaRespuestas(idSeccion: Int): Map<Int, Int> {
        return if (idSeccion == 1) respuestasCatq else respuestasRbq2a
    }

    fun setAyudaVisible(visible: Boolean) {
        mostrarAyudaBottomSheet = visible
    }

    fun reiniciarProgreso() {
        respuestasCatq.clear()
        respuestasRbq2a.clear()
        perfilResultado = null
        prefs.edit().clear().apply()
    }

    // 🌟 CÁLCULO PSICOMÉTRICO REAL EN BASE A LAS RESPUESTAS DEL USUARIO
    fun calcularPerfilReal(): PerfilOrientacion {
        // --- 1. CAT-Q (Compensación: 1, 4, 5, 6, 11, 14, 15, 18, 20)
        val itemsCompensacion = listOf(1, 4, 5, 6, 11, 14, 15, 18, 20)
        val sumaComp = itemsCompensacion.sumOf { respuestasCatq[it] ?: 4 }
        val mediaComp = sumaComp.toFloat() / itemsCompensacion.size.toFloat() // 1.0 a 7.0

        val (nivelComp, colorComp, descComp) = when {
            mediaComp < 3.5f -> Triple("Baja", 0xFF798E9C, "Te desenvuelves de forma espontánea en situaciones sociales.")
            mediaComp <= 5.2f -> Triple("Moderada", 0xFFD99B43, "A veces adaptas tu forma de actuar según el entorno.")
            else -> Triple("Alta", 0xFF5E5A80, "Preparas o ajustas lo que haces para sentirte cómodo/a.")
        }
        val progresoComp = ((mediaComp - 1f) / 6f).coerceIn(0.12f, 0.95f)

        // --- 2. CAT-Q (Enmascaramiento: 2, 7, 8, 9, 10, 13, 17, 25)
        val itemsMascara = listOf(2, 7, 8, 9, 10, 13, 17, 25)
        val sumaMasc = itemsMascara.sumOf { respuestasCatq[it] ?: 4 }
        val mediaMasc = sumaMasc.toFloat() / itemsMascara.size.toFloat() // 1.0 a 7.0

        val (nivelMasc, colorMasc, descMasc) = when {
            mediaMasc < 3.5f -> Triple("Baja", 0xFF798E9C, "Rara vez sientes la necesidad de controlar cómo te perciben.")
            mediaMasc <= 5.2f -> Triple("Moderada", 0xFFD99B43, "Atención a tus gestos y expresiones frente a otros.")
            else -> Triple("Alta", 0xFF5E5A80, "Monitoreas de forma constante cómo te muestras ante los demás.")
        }
        val progresoMasc = ((mediaMasc - 1f) / 6f).coerceIn(0.12f, 0.95f)

        // --- 3. RBQ-2A (Insistencia en rutinas: 1, 4, 6, 7, 9, 10, 11, 13, 14, 15, 17, 19)
        val itemsRutinas = listOf(1, 4, 6, 7, 9, 10, 11, 13, 14, 15, 17, 19)
        val sumaRutinas = itemsRutinas.sumOf { respuestasRbq2a[it] ?: 2 }
        val mediaRutinas = sumaRutinas.toFloat() / itemsRutinas.size.toFloat() // 1.0 a 3.0

        val (nivelRut, colorRut, descRut) = when {
            mediaRutinas < 1.6f -> Triple("Baja", 0xFF798E9C, "Te adaptas con facilidad a variaciones en tus rutinas.")
            mediaRutinas <= 2.2f -> Triple("Moderada", 0xFFD99B43, "Los cambios inesperados te piden más energía.")
            else -> Triple("Alta", 0xFF5E5A80, "Preferencia muy marcada por la predictibilidad y estructura.")
        }
        val progresoRut = ((mediaRutinas - 1f) / 2f).coerceIn(0.12f, 0.95f)

        // --- 4. RBQ-2A (Motor repetitivo / Sensorial: 2, 3, 5, 8, 12, 16, 18, 20)
        val itemsMotor = listOf(2, 3, 5, 8, 12, 16, 18, 20)
        val sumaMotor = itemsMotor.sumOf { respuestasRbq2a[it] ?: 2 }
        val mediaMotor = sumaMotor.toFloat() / itemsMotor.size.toFloat() // 1.0 a 3.0

        val (nivelMot, colorMot, descMot) = when {
            mediaMotor < 1.6f -> Triple("Baja", 0xFF798E9C, "Aparece poco en tus respuestas.")
            mediaMotor <= 2.2f -> Triple("Moderada", 0xFFD99B43, "Aparecen en ciertas ocasiones de concentración o emoción.")
            else -> Triple("Alta", 0xFF5E5A80, "Presentes de forma habitual en momentos de concentración o descanso.")
        }
        val progresoMot = ((mediaMotor - 1f) / 2f).coerceIn(0.12f, 0.95f)

        return PerfilOrientacion(
            esfuerzoSocial = SubescalaResultado("Esfuerzo por adaptarte en lo social", nivelComp, colorComp, progresoComp, descComp),
            cuidarMuestras = SubescalaResultado("Cuidar cómo te muestras", nivelMasc, colorMasc, progresoMasc, descMasc),
            preferenciaPredecible = SubescalaResultado("Preferencia por lo predecible", nivelRut, colorRut, progresoRut, descRut),
            movimientosRepetidos = SubescalaResultado("Movimientos o acciones repetidas", nivelMot, colorMot, progresoMot, descMot)
        )
    }

    fun procesarResultadosConBackend(onCompletado: () -> Unit) {
        viewModelScope.launch {
            // Calculamos el perfil real de inmediato
            perfilResultado = calcularPerfilReal()

            try {
                val catqMap = respuestasCatq.mapKeys { it.key.toString() }
                val rbq2aMap = respuestasRbq2a.mapKeys { it.key.toString() }

                val request = ScreeningRequest(
                    user_id = "USR-APP-LOCAL",
                    catq = if (catqMap.isNotEmpty()) RespuestasCatq(catqMap) else null,
                    rbq2a = if (rbq2aMap.isNotEmpty()) RespuestasRbq2a(rbq2aMap) else null
                )

                ScreeningApiService.procesarScreening(request)
            } catch (_: Exception) {
                delay(1500)
            } finally {
                onCompletado()
            }
        }
    }

    private fun cargarProgreso() {
        for (i in 1..25) {
            if (prefs.contains("catq_p_$i")) {
                respuestasCatq[i] = prefs.getInt("catq_p_$i", 0)
            }
        }
        for (i in 1..20) {
            if (prefs.contains("rbq2a_p_$i")) {
                respuestasRbq2a[i] = prefs.getInt("rbq2a_p_$i", 0)
            }
        }
    }
}