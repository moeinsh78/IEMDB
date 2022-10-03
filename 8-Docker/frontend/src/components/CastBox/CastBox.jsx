import Card from '../Card/Card';
import Carousel from "react-multi-carousel";

function CastBox({cast}) {
    const responsive = {
        desktop: {
          breakpoint: { max: 3000, min: 1024 },
          items: 3
        }
    }

    console.log("Cast Info:", cast)
    return (
        <Carousel swipeable={false}
                draggable={false}
                showDots={false}
                infinite={false}
                responsive={responsive}
                customTransition="all .5"
                containerClass="carousel-container"
                itemClass="carousel-item-padding-40-px">
            {cast.map(({id, name, birthday, image}) => <Card key={id} link={'/actors/'+id} title={name} imageURL={image} text={birthday} hoverInfoClass="hover-info-movie" movieImageClass="cast-movie" hoverItemClass="hover-item-movie" age={true} />)}
        </Carousel>
    );
}

export default CastBox;