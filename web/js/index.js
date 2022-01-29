$(function () {
    $(window).on("scroll", function () {
        if ($(window).scrollTop() > 520) {
            //Añade la propiedad background para que se vuelva del color indicado (el color se define en la parte de los estilos css)
            $(".header").addClass("active");
        } else {
            //Quita la propiedad background para que vuelva a ser transparente (esta fue definida en la parte de los estilos css)
            $(".header").removeClass("active");
        }
    });
});