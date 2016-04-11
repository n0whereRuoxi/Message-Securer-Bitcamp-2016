Message Securer -Version 1.0 -04/10/2016

Developers: Ruizhe Li, Ruoxi Li, Corey Yang
Developers can be reached at:
Email: ruizheli97@gmail.com

General Usage:
—————————————————————————
- The program compresses and secures message and store the encoded information in an mp3, aac, or wav file.
- The program is optimal when encoding financial-related message. e.g, stock price, equity, etc.
- The encoder driver is located in the file EncodeDriver.java, and the decoder driver is located in the file DecoderDriver.java.
- Extra layer of security can be achieved by enabling SECURE in file EncodeDriver.java. If SECURE is enabled, FREQ_FILE with a txt extension must be provided, and should at least contain all characters from basic.txt.
- When SECURE is enabled, the program will be able to learn your encoding history adjust the way it encodes your message each time.
- After the encoder finishes encoding, three new files will be produced: FREQ_TABLE, JUMPER_INFO, and ENCODED_SONG. All three files are required for the decoder to function correctly. Losing any of these three files will result the loss of encoded message.
- After running decoder, DECODED_FILE will be the file name of decoded file.
- For mp3, aac files, a small lose of fidelity will occur after the message is encoded. For wav file, fidelity is preserved. Overall, for fidelity, wav > mp3 > aac.
- JUMPER is highly recommended to be set at 100 for wav file, and 1600 for both mp3 and aac files. This will facilitate the encoding.
- At recommended JUMPER setting, the encoding rate for mp3 and aac files is about 0.01%, which means for each 10MB the program is able to encode roughly 1250 characters (1250 Bytes) into the file. The encoding rate for wav file is about 0.2%, which means for each 10MB the program is able to encode roughly 10,000 characters into the file.
- Max number of characters that set to be allowed to encode is about 2^32, due to the limit of java int size.
- Demo for mp3, aac, and wav are included in the package.
