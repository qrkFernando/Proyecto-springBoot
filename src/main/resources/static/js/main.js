// JavaScript para la Librería Online

// Función para manejar errores de carga de imágenes
function handleImageError(img) {
    console.log('Error cargando imagen:', img.src);
    // Mostrar un placeholder simple en lugar de imagen si falla
    img.style.display = 'none';
    const placeholder = document.createElement('div');
    placeholder.className = 'image-placeholder d-flex align-items-center justify-content-center';
    placeholder.style.cssText = 'height: 300px; background: linear-gradient(135deg, #667eea, #764ba2); color: white; font-weight: bold; text-align: center;';
    placeholder.innerHTML = '📚<br>IMAGEN<br>NO DISPONIBLE';
    img.parentNode.insertBefore(placeholder, img);
}

// Función para configurar el manejo de errores en todas las imágenes
function setupImageErrorHandling() {
    const images = document.querySelectorAll('img.card-img-top');
    images.forEach(img => {
        // Si ya hay un error handler, no lo duplicamos
        if (!img.dataset.errorHandlerSet) {
            img.onerror = function() { handleImageError(this); };
            img.dataset.errorHandlerSet = 'true';
        }
        
        // También agregar loading lazy para mejor performance
        if (!img.loading) {
            img.loading = 'lazy';
        }
    });
}

// Función para mostrar notificaciones toast
function showToast(message, type = 'info') {
    const toastContainer = document.getElementById('toast-container') || createToastContainer();
    
    const toast = document.createElement('div');
    toast.className = `toast align-items-center text-white bg-${type} border-0`;
    toast.setAttribute('role', 'alert');
    toast.innerHTML = `
        <div class="d-flex">
            <div class="toast-body">${message}</div>
            <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast"></button>
        </div>
    `;
    
    toastContainer.appendChild(toast);
    const bsToast = new bootstrap.Toast(toast);
    bsToast.show();
    
    // Remover el toast después de que se oculte
    toast.addEventListener('hidden.bs.toast', () => {
        toast.remove();
    });
}

// Crear contenedor de toasts si no existe
function createToastContainer() {
    const container = document.createElement('div');
    container.id = 'toast-container';
    container.className = 'toast-container position-fixed top-0 end-0 p-3';
    container.style.zIndex = '1050';
    document.body.appendChild(container);
    return container;
}

// Confirmar eliminación de items del carrito
function confirmRemoval(itemName) {
    return confirm(`¿Estás seguro de que quieres eliminar "${itemName}" del carrito?`);
}

// Actualizar cantidad en el carrito
function updateCartQuantity(cartItemId, quantity) {
    if (quantity < 1) {
        if (!confirm('¿Quieres eliminar este producto del carrito?')) {
            return false;
        }
    }
    
    const form = document.createElement('form');
    form.method = 'POST';
    form.action = '/cart/update';
    form.style.display = 'none';
    
    const cartItemInput = document.createElement('input');
    cartItemInput.type = 'hidden';
    cartItemInput.name = 'cartItemId';
    cartItemInput.value = cartItemId;
    
    const quantityInput = document.createElement('input');
    quantityInput.type = 'hidden';
    quantityInput.name = 'quantity';
    quantityInput.value = quantity;
    
    form.appendChild(cartItemInput);
    form.appendChild(quantityInput);
    document.body.appendChild(form);
    form.submit();
}

// Agregar al carrito con animación
function addToCartAnimated(bookId, quantity = 1) {
    const button = event.target;
    const originalText = button.innerHTML;
    
    button.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Agregando...';
    button.disabled = true;
    
    // Simular delay para mostrar el loading
    setTimeout(() => {
        button.innerHTML = '<i class="fas fa-check"></i> Agregado';
        setTimeout(() => {
            button.innerHTML = originalText;
            button.disabled = false;
        }, 1000);
    }, 500);
}

// Búsqueda en tiempo real (opcional)
function setupLiveSearch() {
    const searchInput = document.querySelector('input[name="keyword"]');
    if (searchInput) {
        let timeout;
        searchInput.addEventListener('input', function() {
            clearTimeout(timeout);
            const query = this.value.trim();
            
            if (query.length >= 3) {
                timeout = setTimeout(() => {
                    // Aquí podrías implementar búsqueda AJAX
                    console.log('Buscando:', query);
                }, 300);
            }
        });
    }
}

// Lazy loading para imágenes
function setupLazyLoading() {
    const images = document.querySelectorAll('img[data-src]');
    
    if ('IntersectionObserver' in window) {
        const imageObserver = new IntersectionObserver((entries, observer) => {
            entries.forEach(entry => {
                if (entry.isIntersecting) {
                    const img = entry.target;
                    img.src = img.dataset.src;
                    img.classList.remove('lazy');
                    imageObserver.unobserve(img);
                }
            });
        });
        
        images.forEach(img => imageObserver.observe(img));
    } else {
        // Fallback para navegadores sin soporte
        images.forEach(img => {
            img.src = img.dataset.src;
        });
    }
}

// Smooth scroll para enlaces internos
function setupSmoothScroll() {
    document.querySelectorAll('a[href^="#"]').forEach(anchor => {
        anchor.addEventListener('click', function (e) {
            e.preventDefault();
            const target = document.querySelector(this.getAttribute('href'));
            if (target) {
                target.scrollIntoView({
                    behavior: 'smooth',
                    block: 'start'
                });
            }
        });
    });
}

// Validación de formularios
function setupFormValidation() {
    const forms = document.querySelectorAll('form[data-validate]');
    forms.forEach(form => {
        form.addEventListener('submit', function(e) {
            if (!form.checkValidity()) {
                e.preventDefault();
                e.stopPropagation();
                showToast('Por favor, completa todos los campos requeridos.', 'warning');
            }
            form.classList.add('was-validated');
        });
    });
}

// Funciones de utilidad
const Utils = {
    formatPrice: function(price) {
        return new Intl.NumberFormat('es-ES', {
            style: 'currency',
            currency: 'USD'
        }).format(price);
    },
    
    debounce: function(func, wait) {
        let timeout;
        return function executedFunction(...args) {
            const later = () => {
                clearTimeout(timeout);
                func(...args);
            };
            clearTimeout(timeout);
            timeout = setTimeout(later, wait);
        };
    }
};

// Inicialización cuando el DOM está listo
document.addEventListener('DOMContentLoaded', function() {
    // Inicializar tooltips de Bootstrap
    const tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
    tooltipTriggerList.map(function (tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl);
    });
    
    // Inicializar popovers de Bootstrap
    const popoverTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="popover"]'));
    popoverTriggerList.map(function (popoverTriggerEl) {
        return new bootstrap.Popover(popoverTriggerEl);
    });
    
    // Configurar funcionalidades
    setupImageErrorHandling();
    setupLiveSearch();
    setupLazyLoading();
    setupSmoothScroll();
    setupFormValidation();
    
    // Mostrar mensaje de bienvenida (solo en la página principal)
    if (window.location.pathname === '/') {
        console.log('¡Bienvenido a la Librería Online!');
    }
    
    // Reconfigurar manejo de imágenes después de cargas dinámicas
    setTimeout(setupImageErrorHandling, 1000);
});

// Exportar funciones para uso global
window.LibreriaOnline = {
    showToast,
    confirmRemoval,
    updateCartQuantity,
    addToCartAnimated,
    Utils
};