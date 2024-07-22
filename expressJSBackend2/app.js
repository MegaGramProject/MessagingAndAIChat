const express = require('express');
const { v4: uuidv4 } = require('uuid');
const multer = require('multer');
const cors = require('cors');
const { KeyManagementServiceClient } = require('@google-cloud/kms');
const { Connection, Request, TYPES } = require('tedious');
const sql = require('mssql');
const config = {
    server: '35.225.71.255',
    port: 1433,
    authentication: {
        type: 'default',
        options: {
            userName: 'rishavry',
            password: 'WINwin1$$$'
        }
    },
    options: {
        encrypt: false,
        database: 'Megagram'
    }
};

const configForMSSQL = {
    user: 'rishavry',
    password: 'WINwin1$$$',
    server: '35.225.71.255',
    database: 'Megagram',
    options: {
        encrypt: false,
        enableArithAbort: false
    }
};


const app = express();
const port = 8010;


const keyFilename = '/Users/rishavr/Downloads/megagram-428802-476264306d3b.json';

const client = new KeyManagementServiceClient({ keyFilename });

const upload = multer({
    limits: { fileSize: 1024 * 1024 * 1024 * 10 }
});

const corsOptions = {
    origin: 'http://localhost:8007',
    optionsSuccessStatus: 200
};

app.use(cors(corsOptions));
app.use(express.json({ limit: '10gb' }));


const CHUNK_SIZE = 65000;

app.post('/encryptMessageFiles/:convoid', upload.any(), async (req, res) => {
    try {
        const convoid = req.params.convoid;
        const keyName = client.cryptoKeyPath("megagram-428802", "global", "aiConvoKeys", convoid);
        const output = [];

        const sortedFiles = req.files.sort((a, b) => {
            const indexA = parseInt(a.fieldname.replace('file', ''), 10);
            const indexB = parseInt(b.fieldname.replace('file', ''), 10);
            return indexA - indexB;
        });

        for (let file of sortedFiles) {
            const fileBuffer = file.buffer;
            const numChunks = Math.ceil(fileBuffer.length / CHUNK_SIZE);
            const fileChunks = [];

            for (let i = 0; i < numChunks; i++) {
                const chunk = fileBuffer.slice(i * CHUNK_SIZE, (i + 1) * CHUNK_SIZE);

                const [result] = await client.encrypt({
                    name: keyName,
                    plaintext: chunk
                });

                fileChunks.push(result.ciphertext.toString('base64'));
            }

            if (fileChunks.length > 1) {
                output.push(fileChunks);
            } else {
                output.push(fileChunks[0]);
            }
        }

        res.status(200).json({ encryptedFiles: output });
    } catch (error) {
        console.error('Error encrypting files:', error);
        res.status(500).json({ error: 'Internal Server Error' });
    }
});


const insertFile = (connection, encryptedFile, messageid, convoid, i, fileid) => {
    return new Promise((resolve, reject) => {
    const request = new Request(
        "INSERT INTO aiMessageFiles (fileid, fileName, fileType, encryptedFileBase64String, messageId, convoid, position) VALUES (@fileid, @fileName, @fileType, @encryptedFileBase64String, @messageId, @convoid, @position)",
        (err) => {
        if (err) {
            return reject(err);
        }
        resolve();
        }
    );

    request.addParameter('fileid', TYPES.NVarChar, fileid);
    request.addParameter('fileName', TYPES.NVarChar, encryptedFile[1]);
    request.addParameter('fileType', TYPES.NVarChar, encryptedFile[2]);
    request.addParameter('encryptedFileBase64String', TYPES.NVarChar, Array.isArray(encryptedFile[0]) ? encryptedFile[0][i] : encryptedFile[0]);
    request.addParameter('messageId', TYPES.NVarChar, messageid);
    request.addParameter('convoid', TYPES.NVarChar, convoid);
    request.addParameter('position', TYPES.Int, Array.isArray(encryptedFile[0]) ? i + 1 : 1);

    connection.execSql(request);
    });
};

app.post('/sendFilesWithMessage', async (req, res) => {
try {
    const connection = new Connection(config);

    connection.on('connect', async (err) => {
    if (err) {
        console.error('Connection error:', err);
        return res.status(500).json({ output: 'FAILURE!', error: err.message });
    }

    const messageid = req.body.messageid;
    const convoid = req.body.convoid;
    const encryptedFiles = req.body.emfwd;

    try {
        for (const encryptedFile of encryptedFiles) {
            let fileId = uuidv4();
            if (Array.isArray(encryptedFile[0])) {
                for (let i = 0; i < encryptedFile[0].length; i++) {
                await insertFile(connection, encryptedFile, messageid, convoid, i, fileId);
                }
            } else {
                await insertFile(connection, encryptedFile, messageid, convoid, 0, fileId);
            }
        }

        connection.close();
        res.status(200).json({ output: 'SUCCESS!' });
    } catch (err) {
        console.error('Insert error:', err);
        connection.close();
        res.status(500).json({ output: 'FAILURE!', error: err.message });
    }
    });

    connection.connect();
} catch (error) {
    console.error('Catch error:', error);
    res.status(500).json({ error: error.message });
}
});

app.get('/getFilesForConvo/:convoid', async (req, res) => {
    try {
        await sql.connect(configForMSSQL);

        const convoid = req.params.convoid;

        const request = new sql.Request();

        request.input('convoid', sql.NVarChar, convoid);

        const query = `
            SELECT fileid, fileName, fileType, encryptedFileBase64String, messageId, position
            FROM aiMessageFiles
            WHERE convoid = @convoid
        `;

        const result = await request.query(query);

        res.status(200).json(result.recordset);

    } catch (err) {
        console.error('SQL error:', err);
        res.status(500).json({ output: 'FAILURE!', error: err.message });
    } finally {
        sql.close();
    }
});

app.get('/getFilesForPublicConvo/:convoid', async (req, res) => {
    try {
        await sql.connect(configForMSSQL);

        const convoid = req.params.convoid;

        const request = new sql.Request();

        request.input('convoid', sql.NVarChar, convoid);

        const query = `
            SELECT fileName, fileType, fileBase64String, messageId
            FROM publicAIMessageFiles
            WHERE convoid = @convoid
        `;

        const result = await request.query(query);

        res.status(200).json(result.recordset);

    } catch (err) {
        console.error('SQL error:', err);
        res.status(500).json({ output: 'FAILURE!', error: err.message });
    } finally {
        sql.close();
    }
});


app.post('/decryptMessageFilesForConvo/:convoid', async (req, res) => {
    try {
        const { encryptedMessageFiles } = req.body;
        const convoid = req.params.convoid;
        const keyName = client.cryptoKeyPath("megagram-428802", "global", "aiConvoKeys", convoid);
        const output = [];
        const decryptedFileIds = new Set();
        let allChunks = [];

        for (let encryptedFile of encryptedMessageFiles) {
            if (!decryptedFileIds.has(encryptedFile['fileid'])) {
                decryptedFileIds.add(encryptedFile['fileid']);
                allChunks = encryptedMessageFiles.filter(x => x['fileid'] === encryptedFile['fileid']);
                allChunks.sort((a, b) => a['position'] - b['position']);

                const decryptedChunks = [];
                for (let chunk of allChunks) {
                    const [result] = await client.decrypt({
                        name: keyName,
                        ciphertext: Buffer.from(chunk['encryptedFileBase64String'], 'base64')
                    });
                    decryptedChunks.push(result.plaintext);
                }
                output.push([Buffer.concat(decryptedChunks).toString('base64'), encryptedFile['fileName'], encryptedFile['fileType'], encryptedFile['messageId']]);
            }
        }
        res.status(200).json(output);
    } catch (error) {
        console.error('Error decrypting files:', error);
        res.status(500).json({ error: 'Internal Server Error' });
    }
});


app.post('/addPublicMessageFiles/:convoid', async (req, res) => {
    try {
        const connection = new Connection(config);
    
        connection.on('connect', async (err) => {
        if (err) {
            res.status(500).json({ output: 'FAILURE!', error: err.message });
        }
    
        const convoid = req.params.convoid;
        const messageFiles = req.body.messageFiles;
    
        try {
            for (const messageFile of messageFiles) {
                await insertPublicFile(connection, messageFile[0], messageFile[1], messageFile[2], messageFile[3], convoid);
            }
    
            connection.close();
            res.status(200).json({ output: 'SUCCESS!' });
        } catch (err) {
            connection.close();
            res.status(500).json({ output: 'FAILURE!', error: err.message });
        }
        });
    
        connection.connect();
    } catch (error) {
        connection.close();
        res.status(500).json({ error: error.message });
    }
});

const insertPublicFile = (connection, fileBase64String, fileName, fileType, messageId, convoId) => {
    return new Promise((resolve, reject) => {
    const request = new Request(
        "INSERT INTO publicAIMessageFiles (fileName, fileType, fileBase64String, messageId, convoid) VALUES (@fileName, @fileType, @fileBase64String, @messageId, @convoid)",
        (err) => {
        if (err) {
            return reject(err);
        }
        resolve();
        }
    );

    request.addParameter('fileName', TYPES.NVarChar, fileName);
    request.addParameter('fileType', TYPES.NVarChar, fileType);
    request.addParameter('fileBase64String', TYPES.NVarChar, fileBase64String);
    request.addParameter('messageId', TYPES.NVarChar, messageId);
    request.addParameter('convoid', TYPES.NVarChar, convoId);

    connection.execSql(request);
    });
};


app.listen(port, () => {
    console.log(`Server is running on http://localhost:${port}`);
});

