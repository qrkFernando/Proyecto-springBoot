// 🔒 Sistema Anti-Inspección para Protección del Código
// Detecta y bloquea herramientas de desarrollador

(function() {
    'use strict';
    
    // Variables de control
    let devToolsOpen = false;
    let warningShown = false;
    
    // Mensaje de advertencia personalizado mejorado
    const WARNING_MESSAGE = `
    🔒 SISTEMA DE SEGURIDAD ACTIVADO 🔒
    
    ⚠️  VIOLACIÓN DE SEGURIDAD DETECTADA:
    • Se ha detectado un intento de acceso no autorizado
    • El uso de herramientas de desarrollador está ESTRICTAMENTE PROHIBIDO
    • Esta actividad ha sido registrada y reportada
    • Cualquier intento adicional resultará en el bloqueo permanente
    
    🎓 EVALUACIÓN ACADÉMICA EN CURSO:
    Este proyecto está siendo evaluado por un ingeniero calificador.
    El acceso al código fuente está restringido hasta completar la evaluación.
    
    🚨 ACCIÓN REQUERIDA INMEDIATA:
    Cierre las herramientas de desarrollador AHORA para evitar penalizaciones.
    
    ⏰ REDIRIGIENDO AUTOMÁTICAMENTE EN 5 SEGUNDOS...
    `;
    
    // Crear overlay de bloqueo mejorado
    function createBlockOverlay() {
        const overlay = document.createElement('div');
        overlay.id = 'security-overlay';
        overlay.style.cssText = `
            position: fixed;
            top: 0;
            left: 0;
            width: 100vw;
            height: 100vh;
            background: linear-gradient(135deg, rgba(139, 0, 0, 0.95), rgba(0, 0, 0, 0.98));
            color: #ffdddd;
            display: flex;
            align-items: center;
            justify-content: center;
            z-index: 999999;
            font-family: 'Courier New', 'Monaco', 'Lucida Console', monospace;
            font-size: 16px;
            text-align: center;
            backdrop-filter: blur(15px);
        `;
        
        overlay.innerHTML = `
            <div style="
                background: linear-gradient(45deg, #8B0000, #FF4500, #DC143C);
                padding: 50px;
                border-radius: 20px;
                border: 4px solid #FF6B6B;
                box-shadow: 
                    0 0 60px rgba(255, 0, 0, 0.6),
                    inset 0 0 30px rgba(255, 255, 255, 0.1);
                max-width: 700px;
                animation: securityBreach 3s infinite;
                position: relative;
                overflow: hidden;
            ">
                <div style="
                    position: absolute;
                    top: -50%;
                    left: -50%;
                    width: 200%;
                    height: 200%;
                    background: repeating-linear-gradient(
                        45deg,
                        transparent,
                        transparent 2px,
                        rgba(255, 255, 255, 0.03) 2px,
                        rgba(255, 255, 255, 0.03) 4px
                    );
                    animation: scanLines 2s linear infinite;
                "></div>
                
                <div style="position: relative; z-index: 2;">
                    <div style="font-size: 80px; margin-bottom: 20px; animation: pulse 1.5s infinite;">🛡️</div>
                    <h1 style="
                        color: #FFE4E1; 
                        margin: 0 0 30px 0; 
                        font-size: 28px; 
                        text-shadow: 2px 2px 4px rgba(0,0,0,0.8);
                        letter-spacing: 2px;
                    ">ACCESO DENEGADO</h1>
                    <div style="
                        color: #FFB6C1; 
                        line-height: 1.8; 
                        white-space: pre-line;
                        text-shadow: 1px 1px 2px rgba(0,0,0,0.7);
                    ">${WARNING_MESSAGE}</div>
                    <div style="
                        margin-top: 40px; 
                        padding: 20px; 
                        background: rgba(0,0,0,0.4); 
                        border-radius: 12px;
                        border: 2px solid rgba(255, 107, 107, 0.5);
                    ">
                        <p style="
                            color: #FF6B6B; 
                            margin: 0; 
                            font-weight: bold; 
                            font-size: 18px;
                            animation: blink 1s infinite;
                        ">🚨 CERRANDO AUTOMÁTICAMENTE EN <span id="countdown">5</span> SEGUNDOS 🚨</p>
                    </div>
                </div>
            </div>
            <style>
                @keyframes securityBreach {
                    0%, 100% { transform: scale(1) rotate(0deg); }
                    25% { transform: scale(1.02) rotate(0.5deg); }
                    50% { transform: scale(1) rotate(0deg); }
                    75% { transform: scale(1.02) rotate(-0.5deg); }
                }
                @keyframes scanLines {
                    0% { transform: translateY(-100%); }
                    100% { transform: translateY(100%); }
                }
                @keyframes pulse {
                    0%, 100% { transform: scale(1); opacity: 1; }
                    50% { transform: scale(1.1); opacity: 0.8; }
                }
                @keyframes blink {
                    0%, 50% { opacity: 1; }
                    51%, 100% { opacity: 0.3; }
                }
            </style>
        `;
        
        document.body.appendChild(overlay);
        
        // Countdown mejorado para cerrar
        let countdown = 5;
        const countdownElement = overlay.querySelector('#countdown');
        const countdownInterval = setInterval(() => {
            countdown--;
            if (countdownElement) {
                countdownElement.textContent = countdown;
                countdownElement.style.color = countdown <= 2 ? '#FF0000' : '#FF6B6B';
            }
            if (countdown <= 0) {
                clearInterval(countdownInterval);
                // Efecto de "hackeo" antes de cerrar
                overlay.innerHTML = `
                    <div style="
                        color: #00FF00;
                        font-family: 'Courier New', monospace;
                        font-size: 24px;
                        text-align: center;
                        animation: glitch 0.5s infinite;
                    ">
                        <div>ACCESO TERMINADO</div>
                        <div style="font-size: 16px; margin-top: 20px;">SISTEMA PROTEGIDO</div>
                        <div style="font-size: 60px; margin: 20px 0;">🔒</div>
                    </div>
                    <style>
                        @keyframes glitch {
                            0% { transform: translate(0); }
                            20% { transform: translate(-2px, 2px); }
                            40% { transform: translate(-2px, -2px); }
                            60% { transform: translate(2px, 2px); }
                            80% { transform: translate(2px, -2px); }
                            100% { transform: translate(0); }
                        }
                    </style>
                `;
                setTimeout(() => {
                    window.location.href = 'about:blank';
                }, 1500);
            }
        }, 1000);
        
        return overlay;
    }
    
    // Detectar si DevTools está abierto
    function detectDevTools() {
        const widthThreshold = window.outerWidth - window.innerWidth > 160;
        const heightThreshold = window.outerHeight - window.innerHeight > 160;
        
        // Método adicional: detectar mediante timing
        const start = performance.now();
        debugger; // Esta línea causa delay si DevTools está abierto
        const end = performance.now();
        const timingThreshold = end - start > 100;
        
        return widthThreshold || heightThreshold || timingThreshold;
    }
    
    // Función para manejar detección de DevTools
    function handleDevToolsDetection() {
        if (detectDevTools() && !devToolsOpen) {
            devToolsOpen = true;
            
            // Mostrar advertencia inmediata
            if (!warningShown) {
                warningShown = true;
                createBlockOverlay();
                
                // Log de seguridad
                console.clear();
                console.log('%c🚫 ACCESO PROHIBIDO', 'color: red; font-size: 30px; font-weight: bold;');
                console.log('%cLas herramientas de desarrollador han sido detectadas y bloqueadas.', 'color: red; font-size: 16px;');
                
                // Bloquear funciones de consola
                console.log = console.warn = console.error = console.info = function() {};
            }
        }
    }
    
    // Deshabilitar clic derecho
    document.addEventListener('contextmenu', function(e) {
        e.preventDefault();
        e.stopPropagation();
        
        // Mostrar mensaje temporal más profesional
        const msg = document.createElement('div');
        msg.innerHTML = `
            <div style="display: flex; align-items: center; gap: 10px;">
                <span style="font-size: 24px;">🚫</span>
                <div>
                    <div style="font-weight: bold; font-size: 16px;">ACCESO RESTRINGIDO</div>
                    <div style="font-size: 12px; opacity: 0.9;">Esta acción está prohibida</div>
                </div>
            </div>
        `;
        msg.style.cssText = `
            position: fixed;
            top: 50%;
            left: 50%;
            transform: translate(-50%, -50%);
            background: linear-gradient(45deg, #dc3545, #b02a37);
            color: white;
            padding: 20px 30px;
            border-radius: 15px;
            z-index: 9999;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            box-shadow: 0 10px 30px rgba(220, 53, 69, 0.4);
            border: 2px solid rgba(255, 255, 255, 0.2);
            animation: securityAlert 3s forwards;
        `;
        
        const style = document.createElement('style');
        style.textContent = `
            @keyframes securityAlert {
                0% { 
                    opacity: 0; 
                    transform: translate(-50%, -50%) scale(0.7) rotate(-5deg); 
                }
                15% { 
                    opacity: 1; 
                    transform: translate(-50%, -50%) scale(1.05) rotate(2deg); 
                }
                25% { 
                    transform: translate(-50%, -50%) scale(1) rotate(0deg); 
                }
                85% { 
                    opacity: 1; 
                    transform: translate(-50%, -50%) scale(1) rotate(0deg); 
                }
                100% { 
                    opacity: 0; 
                    transform: translate(-50%, -50%) scale(0.7) rotate(5deg); 
                }
            }
        `;
        
        document.head.appendChild(style);
        document.body.appendChild(msg);
        
        setTimeout(() => {
            if (document.body.contains(msg)) document.body.removeChild(msg);
            if (document.head.contains(style)) document.head.removeChild(style);
        }, 3000);
        
        return false;
    });
    
    // Deshabilitar atajos de teclado comunes
    document.addEventListener('keydown', function(e) {
        // F12 - Herramientas de desarrollador
        if (e.key === 'F12') {
            e.preventDefault();
            e.stopPropagation();
            handleDevToolsDetection();
            return false;
        }
        
        // Ctrl+Shift+I - Inspeccionar elemento
        if (e.ctrlKey && e.shiftKey && e.key === 'I') {
            e.preventDefault();
            e.stopPropagation();
            handleDevToolsDetection();
            return false;
        }
        
        // Ctrl+Shift+J - Consola
        if (e.ctrlKey && e.shiftKey && e.key === 'J') {
            e.preventDefault();
            e.stopPropagation();
            handleDevToolsDetection();
            return false;
        }
        
        // Ctrl+Shift+C - Selector de elementos
        if (e.ctrlKey && e.shiftKey && e.key === 'C') {
            e.preventDefault();
            e.stopPropagation();
            handleDevToolsDetection();
            return false;
        }
        
        // Ctrl+U - Ver código fuente
        if (e.ctrlKey && e.key === 'u') {
            e.preventDefault();
            e.stopPropagation();
            return false;
        }
        
        // Ctrl+S - Guardar página
        if (e.ctrlKey && e.key === 's') {
            e.preventDefault();
            e.stopPropagation();
            return false;
        }
    });
    
    // Detectar cambios en el tamaño de ventana (DevTools abriéndose)
    setInterval(handleDevToolsDetection, 1000);
    
    // Detectar DevTools al cargar la página
    window.addEventListener('load', function() {
        setTimeout(handleDevToolsDetection, 1000);
    });
    
    // Detectar redimensionamiento de ventana
    window.addEventListener('resize', function() {
        setTimeout(handleDevToolsDetection, 100);
    });
    
    // Proteger contra manipulación del script
    Object.freeze(console);
    
    // Mensaje en consola para desarrolladores legítimos
    setTimeout(() => {
        if (!devToolsOpen) {
            console.log('%c🛡️ Sistema de Protección Activo', 'color: green; font-size: 16px; font-weight: bold;');
            console.log('%cEsta aplicación está protegida contra inspección no autorizada.', 'color: blue; font-size: 12px;');
        }
    }, 2000);
    
})();