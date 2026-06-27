# Weather Dashboard - Android App

App Android per visualizzare dati meteorologici in tempo reale da API pubblica.

## 🌤️ Funzionalità Principali

- ✅ **Meteo Attuale** - Temperatura, umidità, velocità vento
- ✅ **Previsioni 5 Giorni** - Forecast giornaliero
- ✅ **Ricerca Città** - Cerca qualsiasi città nel mondo
- ✅ **Città Preferite** - Salva città per accesso rapido
- ✅ **Geolocalizzazione** - Meteo automatico della tua posizione
- ✅ **Grafici Temperatura** - Visualizza andamento temperature
- ✅ **Icone Meteo** - Rappresentazione visiva delle condizioni
- ✅ **Cache Offline** - Dati disponibili anche senza internet
- ✅ **Aggiornamento Automatico** - Sync periodico dei dati

## 🔌 API Meteo

- **OpenWeatherMap API** (gratuita)
- Dati accurati e aggiornati
- Accesso mondiale

## 📚 Stack Tecnologico

- **Linguaggio**: Kotlin
- **Framework**: Android SDK 26+
- **HTTP Client**: Retrofit + OkHttp
- **JSON**: Gson
- **Database**: Room SQLite
- **Charts**: MPAndroidChart
- **UI**: Material Design 3
- **Async**: Coroutines + Flow

## 🎨 Schermate

1. **Home Dashboard** - Meteo attuale e prossime ore
2. **Previsioni** - Forecast 5 giorni con grafici
3. **Ricerca Città** - Search bar con autocomplete
4. **Città Preferite** - Lista città salvate
5. **Dettagli** - Info complete (UV, visibilità, ecc.)

## 🔧 Configurazione

### 1. Ottenere API Key
1. Vai su https://openweathermap.org/api
2. Registrati gratuitamente
3. Copia la tua API Key

### 2. Aggiungere API Key
Crea/modifica `local.properties`:
```properties
OPENWEATHER_API_KEY=your_api_key_here
```

### 3. Installare
```bash
git clone https://github.com/pallantemario0-sudo/weather-dashboard.git
cd weather-dashboard
# Sincronizza Gradle in Android Studio
```

## 📱 Requisiti

- Android 8.0+ (API 26)
- Accesso Internet
- Permessi: INTERNET, ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION

## 🚀 Uso

1. Avvia l'app
2. Consenti accesso alla posizione
3. Visualizza meteo della tua città
4. Cerca altre città
5. Aggiungi ai preferiti
6. Visualizza previsioni e dettagli

## 📊 Dati Visualizzati

- 🌡️ Temperatura attuale e "feels like"
- 💧 Umidità
- 💨 Velocità vento
- 🌅 Alba/Tramonto
- 👁️ Visibilità
- 🔆 Radiazione UV
- 📊 Pressione atmosferica

## 🔄 Aggiornamenti

- Manuale: Pull to refresh
- Automatico: Ogni 10 minuti
- Background: Sync periodico

## 🗄️ Database Locale

- Cache dati meteo
- Lista città preferite
- Storico ricerche
- Funzionamento offline

## 📄 Licenza

MIT License

## 👨‍💻 Autore

Weather Dashboard - 2026
