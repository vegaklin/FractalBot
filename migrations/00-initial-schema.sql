CREATE TABLE chats (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    telegram_chat_id BIGINT NOT NULL UNIQUE
);

CREATE TABLE fractals (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    image BYTEA NOT NULL
);

CREATE TABLE configurations (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    affine_count BIGINT NOT NULL,
    symmetry_count BIGINT NOT NULL,
    samples BIGINT NOT NULL,
    iterations BIGINT NOT NULL,
    image_width BIGINT NOT NULL,
    image_height BIGINT NOT NULL,
    rect_x DOUBLE PRECISION NOT NULL,
    rect_y DOUBLE PRECISION NOT NULL,
    rect_width DOUBLE PRECISION NOT NULL,
    rect_height DOUBLE PRECISION NOT NULL,
    transformation_types TEXT[]
);

CREATE TABLE chat_fractals (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    chat_id BIGINT REFERENCES chats(id) ON DELETE CASCADE,
    fractal_id BIGINT REFERENCES fractals(id) ON DELETE CASCADE,
    configuration_id BIGINT REFERENCES configurations(id) ON DELETE CASCADE,
    create_time TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_chats_id ON chats(id);

CREATE INDEX idx_fractals_id ON fractals(id);

CREATE INDEX idx_configurations_id ON configurations(id);

CREATE INDEX idx_chat_fractals_chat_id ON chat_fractals(chat_id);
CREATE INDEX idx_chat_fractals_fractal_id ON chat_fractals(fractal_id);
CREATE INDEX idx_chat_fractals_configuration_id ON chat_fractals(configuration_id);