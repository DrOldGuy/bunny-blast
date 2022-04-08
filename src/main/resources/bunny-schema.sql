-- This data was taken from: 
-- https://www.coopsandcages.com.au/blog/ultimate-list-rabbit-breeds/

DROP TABLE IF EXISTS breed_category;
DROP TABLE IF EXISTS category;
DROP TABLE IF EXISTS alt_name;
DROP TABLE IF EXISTS breed;

CREATE TABLE breed (
  breed_id int AUTO_INCREMENT NOT NULL,
  breed_name varchar(64) NOT NULL,
  description text,
  PRIMARY KEY (breed_id),
  UNIQUE KEY (breed_name)
);

CREATE TABLE alt_name (
  alternate_id int AUTO_INCREMENT NOT NULL,
  breed_id int NOT NULL,
  alternate_name varchar(64),
  PRIMARY KEY (alternate_id),
  FOREIGN KEY (breed_id) REFERENCES breed (breed_id) ON DELETE CASCADE
);

CREATE TABLE category (
  category_id int AUTO_INCREMENT NOT NULL,
  category_name varchar(32) NOT NULL,
  PRIMARY KEY (category_id),
  UNIQUE KEY (category_name)
);

CREATE TABLE breed_category (
  breed_id int NOT NULL,
  category_id int NOT NULL,
  FOREIGN KEY (breed_id) REFERENCES breed (breed_id) ON DELETE CASCADE,
  FOREIGN KEY (category_id) REFERENCES category (category_id) ON DELETE CASCADE,
  UNIQUE KEY (breed_id, category_id)
);

INSERT INTO breed (breed_id, breed_name, description) VALUES (1, 'American Rabbit', 'American Rabbits made it to the American Rabbit Breeders Association (ARBA) list in 1917. They are known for their fur-like coat and a mandolin body shape. These rabbits are good mothers with a sweet temperament. American Rabbits have a normal size, not a dwarf one. They are raised for their meat and fur.');
INSERT INTO breed (breed_id, breed_name, description) VALUES (2, 'American Sable', 'American Sables are of American origin. They have a body similar to that of Chinchilla’s but with different coat colours. It is dark sepia for their feet, head, back, ears, and the top of their tail. This colour changes into a lighter tan for the rest of their body. They weigh an average of 7 to 15 pounds. American Sables are docile and great sleepers.');
INSERT INTO breed (breed_id, breed_name, description) VALUES (3, 'Angora Rabbit', 'Angora rabbits are among the oldest breeds of domestic rabbit that originated in Angora (formerly known as Ankara), Turkey. They are known for their long, soft wool that is silky. They look like a fur ball, but with a face of a rabbit. Angoras are docile and calm, although they are not as hardy as other breeds. Regular grooming is necessary to prevent matting and felting.');
INSERT INTO breed (breed_id, breed_name, description) VALUES (4, 'Argente Crème', 'Argente rabbits are among the oldest French breeds. They are show rabbits, but are also considered great pets because of their good nature. These rabbits are noted for their slightly arched backs and huge hind quarters despite their small size. Argente rabbits have short and rounded ears that are carried erect on their heads. They can be quite skittish, while females tend to be territorial. The right breed though will be sweet and beautiful.');
INSERT INTO breed (breed_id, breed_name, description) VALUES (5, 'Beige', 'Beige rabbits are believed to have originated in England or in The Netherlands. They are a rare breed with a dense silky fur coloured light-sand with slate blue-ticking. They weigh around 6.5 pounds.');
INSERT INTO breed (breed_id, breed_name, description) VALUES (6, 'Belgian Hare', 'Belgian Hare is a domestic breed that was called as such because of its resemblance to the wild hare. They have agile legs and a long, slender body with a life expectancy of at least ten years. They also have a straight tail, a long head, long and fine-boned fore feet, but fine and flat hind feet. Belgian Hare is known as among the brightest and most active domestic rabbits.');
INSERT INTO breed (breed_id, breed_name, description) VALUES (7, 'Beveren', 'Beverens are among the oldest fur rabbits with a large size. They were developed in Belgium. They have a glossy and dense coat complete with a rollback fur type. These rabbits are often 8-12 pounds. Beverens have V-shaped ears, a mandolin-shaped body, and a full head. Their litters are large and mature fast. They are also clean, smart, and well-tempered.');
INSERT INTO breed (breed_id, breed_name, description) VALUES (8, 'Blanc de Hotot', 'Blanc de Hotot rabbits are of medium size with a thick and compact body. They originated in Normandy, France in the first part of the 1900s. They usually weigh 8-11 pounds. Their fur has extra long guard hairs that make them look like they are covered in frosty sheen. Blanc de Hotots are totally white, except for their narrow black eyeglasses, black eye lashes, black eyelids, and dark brown eyes.');
INSERT INTO breed (breed_id, breed_name, description) VALUES (9, 'Californian', 'Californian rabbits were developed in Southern California by George West. They have big ears, although the Flemish Giants’ ears are still bigger, and are often found in a coat that is the same as that of a Himalayan rabbit. These rabbits are quiet and reserved. Californians are great house pets.');
INSERT INTO breed (breed_id, breed_name, description) VALUES (10, 'Checked Giant', 'Checkered Giant rabbits originated in Europe. They usually weigh more than 11 pounds. At first look, these rabbits look almost like the English Spot breed. They are noted for the butterfly pattern on their nose and their solid blue or black, erect ears. They also have two blue or black spots on either side of their body plus a blue or black stripe that runs from the tail to the base of the ears. Checkered Giants make good pets, trainable, and are regular groomers.');
INSERT INTO breed (breed_id, breed_name, description) VALUES (11, 'Chinchilla', 'Chinchilla rabbits are named as such because of their resemblance to chinchillas. The Standard Chinchilla and the Giganta breed were both developed in France, while the American and Giant Chinchilla originated in the US. They usually weigh around 10 to 16 pounds. They have a dense but soft coat of hair that has different colour bands. Chinchilla rabbits are good natured, docile, and gentle.');
INSERT INTO breed (breed_id, breed_name, description) VALUES (12, 'Cinnamon', 'Cinnamon rabbits are domestic breeds developed in the US in 1962. They are best known for their russet-coloured fur, which earned them their name. Their average weight ranges from 8.5 to 11 pounds. Their coat also has a smoky grey colouring on their sides and a dark underbelly. They also have rust-coloured spots on their feet and face. Cinnamon rabbits are calm, laid-back, and love the attention of their owners.');
INSERT INTO breed (breed_id, breed_name, description) VALUES (13, 'Dutch Rabbit', 'Dutch Rabbits used to be the most popular rabbit breed, but were replaced by the popularity of dwarf rabbits. Despite their name, they were actually first bred in England. They have delicate bodies that should be handled with extra care, which is why pet owners need proper training, to prevent injuries. These rabbits have very small, erect ears, powerful back legs that are bigger than their front feet, and a generally white coat with some base colours. Dutch Rabbits are gentle with a good disposition.');
INSERT INTO breed (breed_id, breed_name, description) VALUES (14, 'Dwarf Lop', 'Dwarf Lops are popular show rabbit breeds in the US. It was discovered in Essen, Germany in 1972. They often weigh around 8 pounds and have thick ears that are soft and drooping. They are found in more than 40 colours. These rabbits don’t have problems with other pets. They are easy to handle but strong enough to survive cuddling from children.');
INSERT INTO breed (breed_id, breed_name, description) VALUES (15, 'English Spot', 'English Spots originated in England. They usually weigh around 5 to 8 pounds. They are known for the unique markings on their body, including eye circles, the butterfly mark, herringbone, coloured ears, cheek spots, and a chain of small spots. English Spots are fun and curious. However, they need one to two hours of activities daily.');
INSERT INTO breed (breed_id, breed_name, description) VALUES (16, 'English Lop', 'English Lops originated in England in the 19th century. They are the original Lop with a body that is more slender and longer than the body of the other types of Lops. They are also noted for their long ears that average 22 inches, making them the rabbit with the largest ears. English Lops are laid-back and calm, but they need proper handling to prevent them from being easily scared.');
INSERT INTO breed (breed_id, breed_name, description) VALUES (17, 'Flemish Giant', 'Flemish Giants are domestic rabbits developed in Flanders in Belgium. Their average weight is around 13 to 14 pounds. They have a long powerful body with a white underside and a dark base colouring. They also have broad hind quarters. Flemish Giants are docile and tolerant of being handled only if they have frequent and proper interaction with humans.');
INSERT INTO breed (breed_id, breed_name, description) VALUES (18, 'French Lop', 'French Lops originated in France and were bred in 1850. They were bred for their meat. They look like Flemish Giants, but with a shorter body and an average weight of 15 pounds. They also have ears that are 5 to 8 inches in length and a cubic appearance with their large head and thick body. French Lops need to occupy a large space because of their size and can be placed indoors or outdoors. They are quite stubborn when it comes to litter-box training.');
INSERT INTO breed (breed_id, breed_name, description) VALUES (19, 'Golden Glavcot', 'Golden Glavcots are of English origin. They have a golden roan coat and fine bones. They also have a mandolin-shaped body, proportionate and erect ears, and a moderate-sized head. Their legs and feet are fine and straight with a blueish undercolour. They have a straight tail with the same colour as their coat. Golden Glavcots are affectionate and friendly. If they are introduced to humans properly, they will grow up cuddly.');
INSERT INTO breed (breed_id, breed_name, description) VALUES (20, 'Harlequin', 'Harlequins are of French origin. They weigh around 7.5 pounds. Their unique colour patterns make them stand out in addition to their bumblebee-like appearance. Their most common varieties are the Magpie and the Japanese. The Japanese are mostly orange with markings of blue, black, lilac or chocolate. The Magpies have the same markings but their base colour is white. Harlequins are good pets to snuggle with but if they are raised for exhibition, pet owners should be patient and persistent.');
INSERT INTO breed (breed_id, breed_name, description) VALUES (21, 'Havana', 'Havanas originated in The Netherlands. They weigh around 4.5 to 6.5 pounds. They have a well-rounded and compact body. Their ears are erect and short when compared to their bodies. Their head is safely nestled against their shoulders so their neck is hidden. The Havanas’ coat is glossy, dense and short. Their original coat colour is chocolate, although blue, black, lilac, and brokens are already accepted. These rabbits make good pets and are friendly and docile.');
INSERT INTO breed (breed_id, breed_name, description) VALUES (22, 'Himalayan', 'Himalayans are of Asian origin. They look almost like the Californian rabbits. They weigh around 2.5 to 4.5 pounds. They are svelte and look similar to a cat. Their body shape is described as tube-like or cylindrical. Himalayans have a purely white coat with markings on the eye circles, feet, tail, and nose. They are known for being the only rabbit breed with an extra set of nipples. They are also easygoing and calm, making them good companions.');
INSERT INTO breed (breed_id, breed_name, description) VALUES (23, 'Holland Lop', 'Holland Lops originated in The Netherlands. They are popular show rabbits and pets because they are sweet and not aggressive. They are compact and stocky with a thickset body, short, strong legs, and rounded haunches. They also have a dense coat made of medium length hair.');
INSERT INTO breed (breed_id, breed_name, description) VALUES (24, 'Hulstlander', 'Hulstlanders originated in The Netherlands. They are of small to medium size and are mostly known for their pure white coat and pale blue eyes. These rabbits have a short body with a well-developed front and hindquarters. They have short and sturdy front legs.');
INSERT INTO breed (breed_id, breed_name, description) VALUES (25, 'Jersey Wooly', 'Jersey Wooly are domestic rabbits of American origin. They are known for their easy-care wooly fur and a bold head. They weigh around 2.5 to 3.5 pounds. They have a compact body as well as small and erect ears. Jersey Wooly rabbits are playful, affectionate, and gentle.');
INSERT INTO breed (breed_id, breed_name, description) VALUES (26, 'Lionhead', 'Lionhead rabbits were developed in Belgium. They are small with a compact body, a bold head, and a well-developed muzzle. They have medium length legs and ears that are not more than three inches long. Lionheads are well mannered and friendly. They are smart and can be trained.');
INSERT INTO breed (breed_id, breed_name, description) VALUES (27, 'Netherland Dwarf', 'The Netherland Dwarf was developed in The Netherlands. They are smaller than other rabbits with an average weight of 500 g to 1.6 kg. They are usually raised as exhibition rabbits or pets. The Netherland Dwarf has relatively large head and eyes compared to their small body. They have tiny ears that stand erect on their head. They look like infants even way into their adulthood. These rabbits share the same traits as cats and dogs.');
INSERT INTO breed (breed_id, breed_name, description) VALUES (28, 'New Zealand Rabbit', 'New Zealand rabbits are of American origin. They are raised for meat and for laboratory experiments. However, they are also popular show rabbits and pets. Adult rabbits have an average weight of 12 pounds with well-rounded haunches, a medium-sized body, a bold head, and short front legs. Their coat is soft and dense.');
INSERT INTO breed (breed_id, breed_name, description) VALUES (29, 'Perlfee', 'Perlfee are cobby rabbits with rounded hindquarters and a broad chest. They weigh 5.5 to 8 pounds. They have a firm body, an invisible neck, and erect, well-furred ears. Their fur is dense but silky smooth. Perlfee’s coat is mainly greyish blue and is found in three shades – light, medium, and dark. Their undercolour is grey blue and an intermediate colour.');
INSERT INTO breed (breed_id, breed_name, description) VALUES (30, 'Polish Rabbit', 'Polish Rabbits are domestic rabbits believed to have originated in England. They are raised as pets and as show rabbits. They are small with short ears, short head, bold eyes, and full cheeks. Their small size often has people confuse them with the Netherland dwarf. Polish rabbits occupy only a small space. They are calm and friendly, although they need to be spayed to prevent any territorial behaviour.');
INSERT INTO breed (breed_id, breed_name, description) VALUES (31, 'Rex', 'Rex rabbits are of French origin. They are known for their plush, dense, and velvet-like fur. They have a medium-sized, round body that weighs an average of 7.5 to 10.5 pounds. Rex rabbits have a broader head, upright ears that are proportionate to their size, and toe nails with the same colour as their fur. They are believed to be among the smartest breeds of rabbits.');
INSERT INTO breed (breed_id, breed_name, description) VALUES (32, 'Rhinelander', 'Rhinelander rabbits are of German origin. They are mostly noted for their butterfly markings, usually coloured orange and black, on their white fur. They have an arched body with short legs. Their body shape is best described as cylindrical or barrel-like. Rhinelanders weigh around 6 to 10 pounds.');
INSERT INTO breed (breed_id, breed_name, description) VALUES (33, 'Satin', 'Satin rabbits are of American origin. They are called as such because of their coat’s striking satin sheen. They are of medium to large size with fairly broad, strong legs, arched body, and a broad head. Their dense coat is made of silky, fine, medium length hair. Satin rabbits are good natured and calm. They are friendly, gentle, and good companions for children.');
INSERT INTO breed (breed_id, breed_name, description) VALUES (34, 'Silver', 'Silver rabbits were developed in the 1500s, making them among the oldest breeds of rabbits in the world. They are small rabbits with an average weight of 5 to 6 pounds. They are found in three varieties – brown, black, and fawn. Silver rabbits are called as such because of their silver-white hairs as well as hair tips.');
INSERT INTO breed (breed_id, breed_name, description) VALUES (35, 'Silver Fox', 'Silver Fox rabbits were developed in North Canton, Ohio. They are raised for their fur, meat, and popularity in exhibitions. Adult bucks weigh around 9 to 11 pounds, while adult does are heavier at around 10 to 12 pounds. They are found in colours chocolate, blue, and lilac with the less common colours being smoke pearl and white. Silver Fox rabbits love attention and handling. They are friendly and are easy to pose.');
INSERT INTO breed (breed_id, breed_name, description) VALUES (36, 'Silver Marten', 'Silver Martens are domestic rabbits that weigh around 6.5 to 8.5 pounds. They have a soft and polished fur. They are hardy breeds that are more timid than the other rabbits. However, they still make good pets because they are playful and love to have playthings in their cage. Silver Marten’s most common varieties have coats that are coloured black, blue, sable, and chocolate.');
INSERT INTO breed (breed_id, breed_name, description) VALUES (37, 'Tan', 'Tan rabbits are of English origin, although they have recently become popular in the US. They weigh around 4 to 6 pounds and are available in blue, black, lilac, and chocolate. They have an arched and compact body. They stand out mostly because of their unique markings that are in huge contrast to their general colour. Tan rabbits need regular exercise.');
INSERT INTO breed (breed_id, breed_name, description) VALUES (38, 'Thrianta', 'Thrianta rabbits were developed in The Netherlands. They are red rabbits with the fawn colouring on their tail and paws. They have a short and soft, medium length fur. Adults weigh around 5 to 6 pounds. Thriantas are gentle and curious. They make good starter pets for children.');
INSERT INTO breed (breed_id, breed_name, description) VALUES (39, 'Thuringer', 'Thuringer rabbits are of German origin. They have a deep yellow ochre colour with blue-black ticking. They are usually raised for their meat and fur. Thuringers are quite stocky. Their neck usually doesn’t show and their legs are sturdy. Their ears are covered with fur and stand erect on their head. These rabbits are calm and pleasant, making them good pets and show rabbits.');

INSERT INTO alt_name (alternate_id, breed_id, alternate_name) VALUES (1, 3, 'English Angora Rabbit');
INSERT INTO alt_name (alternate_id, breed_id, alternate_name) VALUES (2, 10, 'Giant Papillon');
INSERT INTO alt_name (alternate_id, breed_id, alternate_name) VALUES (3, 13, 'The Hollander');
INSERT INTO alt_name (alternate_id, breed_id, alternate_name) VALUES (4, 14, 'Mini Lop');
INSERT INTO alt_name (alternate_id, breed_id, alternate_name) VALUES (5, 14, 'Klein Widder');
INSERT INTO alt_name (alternate_id, breed_id, alternate_name) VALUES (6, 30, 'Britannia Petite (in the US)');

INSERT INTO category (category_id, category_name) VALUES (1, 'smooth');
INSERT INTO category (category_id, category_name) VALUES (2, 'fuzzy');
INSERT INTO category (category_id, category_name) VALUES (3, 'lop-eared');
INSERT INTO category (category_id, category_name) VALUES (4, 'spotted');

INSERT INTO breed_category (breed_id, category_id) VALUES (1, 1);
INSERT INTO breed_category (breed_id, category_id) VALUES (2, 1);
INSERT INTO breed_category (breed_id, category_id) VALUES (3, 2);
INSERT INTO breed_category (breed_id, category_id) VALUES (4, 1);
INSERT INTO breed_category (breed_id, category_id) VALUES (5, 1);
INSERT INTO breed_category (breed_id, category_id) VALUES (6, 1);
INSERT INTO breed_category (breed_id, category_id) VALUES (7, 1);
INSERT INTO breed_category (breed_id, category_id) VALUES (8, 1);
INSERT INTO breed_category (breed_id, category_id) VALUES (9, 1);
INSERT INTO breed_category (breed_id, category_id) VALUES (10, 1);
INSERT INTO breed_category (breed_id, category_id) VALUES (10, 4);
INSERT INTO breed_category (breed_id, category_id) VALUES (11, 1);
INSERT INTO breed_category (breed_id, category_id) VALUES (12, 1);
INSERT INTO breed_category (breed_id, category_id) VALUES (13, 1);
INSERT INTO breed_category (breed_id, category_id) VALUES (13, 4);
INSERT INTO breed_category (breed_id, category_id) VALUES (14, 1);
INSERT INTO breed_category (breed_id, category_id) VALUES (14, 3);
INSERT INTO breed_category (breed_id, category_id) VALUES (14, 4);
INSERT INTO breed_category (breed_id, category_id) VALUES (15, 1);
INSERT INTO breed_category (breed_id, category_id) VALUES (15, 4);
INSERT INTO breed_category (breed_id, category_id) VALUES (16, 1);
INSERT INTO breed_category (breed_id, category_id) VALUES (16, 3);
INSERT INTO breed_category (breed_id, category_id) VALUES (16, 4);
INSERT INTO breed_category (breed_id, category_id) VALUES (17, 1);
INSERT INTO breed_category (breed_id, category_id) VALUES (18, 1);
INSERT INTO breed_category (breed_id, category_id) VALUES (18, 3);
INSERT INTO breed_category (breed_id, category_id) VALUES (19, 1);
INSERT INTO breed_category (breed_id, category_id) VALUES (20, 1);
INSERT INTO breed_category (breed_id, category_id) VALUES (21, 1);
INSERT INTO breed_category (breed_id, category_id) VALUES (22, 1);
INSERT INTO breed_category (breed_id, category_id) VALUES (22, 4);
INSERT INTO breed_category (breed_id, category_id) VALUES (23, 1);
INSERT INTO breed_category (breed_id, category_id) VALUES (23, 3);
INSERT INTO breed_category (breed_id, category_id) VALUES (23, 4);
INSERT INTO breed_category (breed_id, category_id) VALUES (24, 1);
INSERT INTO breed_category (breed_id, category_id) VALUES (25, 1);
INSERT INTO breed_category (breed_id, category_id) VALUES (26, 2);
INSERT INTO breed_category (breed_id, category_id) VALUES (27, 1);
INSERT INTO breed_category (breed_id, category_id) VALUES (28, 1);
INSERT INTO breed_category (breed_id, category_id) VALUES (29, 1);
INSERT INTO breed_category (breed_id, category_id) VALUES (30, 1);
INSERT INTO breed_category (breed_id, category_id) VALUES (31, 1);
INSERT INTO breed_category (breed_id, category_id) VALUES (31, 4);
INSERT INTO breed_category (breed_id, category_id) VALUES (32, 1);
INSERT INTO breed_category (breed_id, category_id) VALUES (32, 4);
INSERT INTO breed_category (breed_id, category_id) VALUES (33, 1);
INSERT INTO breed_category (breed_id, category_id) VALUES (34, 1);
INSERT INTO breed_category (breed_id, category_id) VALUES (35, 1);
INSERT INTO breed_category (breed_id, category_id) VALUES (36, 1);
INSERT INTO breed_category (breed_id, category_id) VALUES (37, 1);
INSERT INTO breed_category (breed_id, category_id) VALUES (38, 1);
INSERT INTO breed_category (breed_id, category_id) VALUES (39, 1);
