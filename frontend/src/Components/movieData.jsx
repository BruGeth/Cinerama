// Array containing movie details, including metadata and showtimes per format.
export const MOVIES = [
  {
    id: 1,// Unique identifier for the movie
    title: 'Thunderbolts',
    description: 'Un mundo sin Vengadores no significa que no haya un grupo de superhéroes. Hay un grupo y se llaman Thunderbolts.',
    image: 'https://www.cinerama.com.pe/_admin/assets/images/peliculas/thunderbolts-poster-66f184dbe403e.jpg',
    duration: 120,
    genre: 'Acción',
    showtimes: {// Available showtimes categorized by format
      "2D": ['13:30', '16:10', '18:50', '21:30'],
      "3D": ['14:00', '17:00', '20:00'],
      "XD": ['15:00', '18:30', '22:00']
    }
  },
  {
    id: 2,
    title: 'Destino Final: Lazos de Sangre',
    description: 'Un adolescente tiene una visión de él y sus amigos muriendo en un accidente de avión. Previene el accidente, pero la muerte los persigue uno por uno.',
    image: '/images/Home1.jpg',
    duration: 95,
    genre: 'Terror',
    showtimes: {
      "2D": ['14:00', '16:30', '19:00', '21:30'],
      "3D": ['15:00', '18:00', '21:00'],
      "XD": ['16:00', '19:30', '22:00']
    }
  },
  {
    id: 3,
    title: 'Karate Kid Leyendas',
    description: 'Daniel LaRusso y su madre acaban de mudarse a Reseda, Los Ángeles, desde Newark, Nueva Jersey, al comenzar el año escolar.',
    image: '/images/Home3.jpg',
    duration: 110,
    genre: 'Drama',
    showtimes: {
      "2D": ['12:40', '15:10', '17:40', '20:10'],
      "3D": ['13:30', '16:30', '19:30'],
      "XD": ['14:30', '17:30', '20:30']
    }
  },
  {
    id: 4,
    title: 'Star Wars: Episodio III - La venganza de los Sith',
    description: 'El Canciller Palpatine fue secuestrado y el Maestro Jedi Obi-Wan Kenobi, acompañado de su aprendiz Anakin Skywalker...',
    image: 'https://www.cinerama.com.pe/_admin/assets/images/peliculas/star_wars_episode_iii_revenge_of_the_sith-477144354-large.jpg',
    duration: 140,
    genre: 'Ciencia Ficción',
    showtimes: {
      "2D": ['13:00', '15:50', '18:40', '21:30'],
      "3D": ['14:30', '17:30', '20:30'],
      "XD": ['15:30', '18:30', '21:30']
    }
  },
  {
    id: 5,
    title: 'THE AMATHEUR: OPERACIÓN VENGANZA',
    description: 'Charlie Heller, un descodificador de la CIA, tras perder a su esposa en un atentado, se ve obligado a tomar justicia por su cuenta...',
    image: 'https://www.cinerama.com.pe/_admin/assets/images/peliculas/amateur.jpg',
    duration: 105,
    genre: 'Thriller',
    showtimes: {
      "2D": ['14:30', '17:00', '19:30', '22:00'],
      "3D": ['15:30', '18:30', '21:30'],
      "XD": ['16:30', '19:30', '22:30']
    }
  },
  {
    id: 6,
    title: 'UNTIL DAWN: NOCHE DE TERROR',
    description: 'La historia sigue a un grupo de amigos atrapados en un ciclo de terror donde un asesino los persigue y mata uno a uno...',
    image: 'https://www.cinerama.com.pe/_admin/assets/images/peliculas/until_dawn-880447124-large.jpg',
    duration: 100,
    genre: 'Suspenso',
    showtimes: {
      "2D": ['13:20', '15:50', '18:20', '20:50'],
      "3D": ['14:20', '17:20', '20:20'],
      "XD": ['15:20', '18:20', '21:20']
    }
  }
];
