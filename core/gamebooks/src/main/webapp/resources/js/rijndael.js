function doFullDecrypt(a) {
	var b = rijndaelDecrypt(hexToByteArray(a));
	return byteArrayToString(b);
}
function doFullDecryptFT(a, b) {
	var c = document.getElementById(a).value;
	var d = rijndaelDecrypt(hexToByteArray(c));
	document.getElementById(b).value = byteArrayToString(d);
}
function doFullEncryptFT(a, b) {
	var c = document.getElementById(a).value;
	var d = rijndaelEncrypt(c);
	document.getElementById(b).value = byteArrayToHex(d);
}
function rijndaelDecrypt(a, b) {
	var c;
	var d = blockSizeInBits / 8;
	var e = new Array;
	var f;
	var g;
	var h = hexToByteArray("a0848c8a0ab2fb72568a61ebae480bc35752e245e90bd0539282f02168b3671a");
	if (!a || !h || typeof a == "string")
		return;
	if (h.length * 8 != keySizeInBits)
		return;
	if (!b)
		b = "ECB";
	c = keyExpansion(h);
	for (g = a.length / d - 1; g > 0; g--) {
		f = decrypt(a.slice(g * d, (g + 1) * d), c);
		if (b == "CBC")
			for ( var i = 0; i < d; i++)
				e[(g - 1) * d + i] = f[i] ^ a[(g - 1) * d + i];
		else
			e = f.concat(e);
	}
	if (b == "ECB")
		e = decrypt(a.slice(0, d), c).concat(e);
	return e;
}
function rijndaelEncrypt(a, b) {
	var c, d, e;
	var f = blockSizeInBits / 8;
	var g;
	var h = hexToByteArray("a0848c8a0ab2fb72568a61ebae480bc35752e245e90bd0539282f02168b3671a");
	if (!a || !h)
		return;
	if (h.length * 8 != keySizeInBits)
		return;
	if (b == "CBC")
		g = getRandomBytes(f);
	else {
		b = "ECB";
		g = new Array();
	}
	a = formatPlaintext(a);
	c = keyExpansion(h);
	for ( var i = 0; i < a.length / f; i++) {
		e = a.slice(i * f, (i + 1) * f);
		if (b == "CBC")
			for ( var d = 0; d < f; d++)
				e[d] ^= g[i * f + d];
		g = g.concat(encrypt(e, c));
	}
	return g;
}
function getRandomBytes(a) {
	var b;
	var c = new Array;
	for (b = 0; b < a; b++)
		c[b] = Math.round(Math.random() * 255);
	return c;
}
function formatPlaintext(a) {
	var b = blockSizeInBits / 8;
	var c;
	if (typeof a == "string" || a.indexOf) {
		a = a.split("");
		for (c = 0; c < a.length; c++)
			a[c] = a[c].charCodeAt(0) & 255;
	}
	for (c = b - a.length % b; c > 0 && c < b; c--)
		a[a.length] = 0;
	return a;
}
function unpackBytes(a) {
	var b = new Array;
	for ( var c = 0; c < a[0].length; c++) {
		b[b.length] = a[0][c];
		b[b.length] = a[1][c];
		b[b.length] = a[2][c];
		b[b.length] = a[3][c];
	}
	return b;
}
function packBytes(a) {
	var b = new Array;
	if (!a || a.length % 4)
		return;
	b[0] = new Array;
	b[1] = new Array;
	b[2] = new Array;
	b[3] = new Array;
	for ( var c = 0; c < a.length; c += 4) {
		b[0][c / 4] = a[c];
		b[1][c / 4] = a[c + 1];
		b[2][c / 4] = a[c + 2];
		b[3][c / 4] = a[c + 3];
	}
	return b;
}
function hexToByteArray(a) {
	var b = [];
	if (a.length % 2)
		return;
	if (a.indexOf("0x") == 0 || a.indexOf("0X") == 0)
		a = a.substring(2);
	for ( var c = 0; c < a.length; c += 2)
		b[Math.floor(c / 2)] = parseInt(a.slice(c, c + 2), 16);
	return b;
}
function byteArrayToHex(a) {
	var b = "";
	if (!a)
		return;
	for ( var c = 0; c < a.length; c++)
		b += (a[c] < 16 ? "0" : "") + a[c].toString(16);
	return b;
}
function byteArrayToString(a) {
	var b = "";
	for ( var c = 0; c < a.length; c++)
		if (a[c] != 0)
			b += String.fromCharCode(a[c]);
	return b;
}
function decrypt(a, b) {
	var c;
	if (!a || a.length * 8 != blockSizeInBits)
		return;
	if (!b)
		return;
	a = packBytes(a);
	InverseFinalRound(a, b.slice(Nb * Nr));
	for (c = Nr - 1; c > 0; c--)
		InverseRound(a, b.slice(Nb * c, Nb * (c + 1)));
	addRoundKey(a, b);
	return unpackBytes(a);
}
function encrypt(a, b) {
	var c;
	if (!a || a.length * 8 != blockSizeInBits)
		return;
	if (!b)
		return;
	a = packBytes(a);
	addRoundKey(a, b);
	for (c = 1; c < Nr; c++)
		Round(a, b.slice(Nb * c, Nb * (c + 1)));
	FinalRound(a, b.slice(Nb * Nr));
	return unpackBytes(a);
}
function InverseFinalRound(a, b) {
	addRoundKey(a, b);
	shiftRow(a, "decrypt");
	byteSub(a, "decrypt");
}
function FinalRound(a, b) {
	byteSub(a, "encrypt");
	shiftRow(a, "encrypt");
	addRoundKey(a, b);
}
function InverseRound(a, b) {
	addRoundKey(a, b);
	mixColumn(a, "decrypt");
	shiftRow(a, "decrypt");
	byteSub(a, "decrypt");
}
function Round(a, b) {
	byteSub(a, "encrypt");
	shiftRow(a, "encrypt");
	mixColumn(a, "encrypt");
	addRoundKey(a, b);
}
function keyExpansion(a) {
	var b = new Array;
	var c;
	Nk = keySizeInBits / 32;
	Nb = blockSizeInBits / 32;
	Nr = roundsArray[Nk][Nb];
	for ( var d = 0; d < Nk; d++)
		b[d] = a[4 * d] | a[4 * d + 1] << 8 | a[4 * d + 2] << 16
				| a[4 * d + 3] << 24;
	for (d = Nk; d < Nb * (Nr + 1); d++) {
		c = b[d - 1];
		if (d % Nk == 0)
			c = (SBox[c >> 8 & 255] | SBox[c >> 16 & 255] << 8
					| SBox[c >> 24 & 255] << 16 | SBox[c & 255] << 24)
					^ Rcon[Math.floor(d / Nk) - 1];
		else if (Nk > 6 && d % Nk == 4)
			c = SBox[c >> 24 & 255] << 24 | SBox[c >> 16 & 255] << 16
					| SBox[c >> 8 & 255] << 8 | SBox[c & 255];
		b[d] = b[d - Nk] ^ c;
	}
	return b;
}
function addRoundKey(a, b) {
	for ( var c = 0; c < Nb; c++) {
		a[0][c] ^= b[c] & 255;
		a[1][c] ^= b[c] >> 8 & 255;
		a[2][c] ^= b[c] >> 16 & 255;
		a[3][c] ^= b[c] >> 24 & 255;
	}
}
function mixColumn(a, b) {
	var c = [];
	for ( var d = 0; d < Nb; d++) {
		for ( var e = 0; e < 4; e++) {
			if (b == "encrypt")
				c[e] = mult_GF256(a[e][d], 2)
						^ mult_GF256(a[(e + 1) % 4][d], 3) ^ a[(e + 2) % 4][d]
						^ a[(e + 3) % 4][d];
			else
				c[e] = mult_GF256(a[e][d], 14)
						^ mult_GF256(a[(e + 1) % 4][d], 11)
						^ mult_GF256(a[(e + 2) % 4][d], 13)
						^ mult_GF256(a[(e + 3) % 4][d], 9);
		}
		for ( var e = 0; e < 4; e++)
			a[e][d] = c[e];
	}
}
function shiftRow(a, b) {
	for ( var c = 1; c < 4; c++)
		if (b == "encrypt")
			a[c] = cyclicShiftLeft(a[c], shiftOffsets[Nb][c]);
		else
			a[c] = cyclicShiftLeft(a[c], Nb - shiftOffsets[Nb][c]);
}
function byteSub(a, b) {
	var c;
	if (b == "encrypt")
		c = SBox;
	else
		c = SBoxInverse;
	for ( var d = 0; d < 4; d++)
		for ( var e = 0; e < Nb; e++)
			a[d][e] = c[a[d][e]];
}
function mult_GF256(a, b) {
	var c, d = 0;
	for (c = 1; c < 256; c *= 2, b = xtime(b)) {
		if (a & c)
			d ^= b;
	}
	return d;
}
function xtime(a) {
	a <<= 1;
	return a & 256 ? a ^ 283 : a;
}
function cyclicShiftLeft(a, b) {
	var c = a.slice(0, b);
	a = a.slice(b).concat(c);
	return a;
}
var keySizeInBits = 256;
var blockSizeInBits = 256;
var roundsArray = [ , , , , [ , , , , 10, , 12, , 14 ], ,
		[ , , , , 12, , 12, , 14 ], , [ , , , , 14, , 14, , 14 ] ];
var shiftOffsets = [ , , , , [ , 1, 2, 3 ], , [ , 1, 2, 3 ], , [ , 1, 3, 4 ] ];
var Rcon = [ 1, 2, 4, 8, 16, 32, 64, 128, 27, 54, 108, 216, 171, 77, 154, 47,
		94, 188, 99, 198, 151, 53, 106, 212, 179, 125, 250, 239, 197, 145 ];
var SBox = [ 99, 124, 119, 123, 242, 107, 111, 197, 48, 1, 103, 43, 254, 215,
		171, 118, 202, 130, 201, 125, 250, 89, 71, 240, 173, 212, 162, 175,
		156, 164, 114, 192, 183, 253, 147, 38, 54, 63, 247, 204, 52, 165, 229,
		241, 113, 216, 49, 21, 4, 199, 35, 195, 24, 150, 5, 154, 7, 18, 128,
		226, 235, 39, 178, 117, 9, 131, 44, 26, 27, 110, 90, 160, 82, 59, 214,
		179, 41, 227, 47, 132, 83, 209, 0, 237, 32, 252, 177, 91, 106, 203,
		190, 57, 74, 76, 88, 207, 208, 239, 170, 251, 67, 77, 51, 133, 69, 249,
		2, 127, 80, 60, 159, 168, 81, 163, 64, 143, 146, 157, 56, 245, 188,
		182, 218, 33, 16, 255, 243, 210, 205, 12, 19, 236, 95, 151, 68, 23,
		196, 167, 126, 61, 100, 93, 25, 115, 96, 129, 79, 220, 34, 42, 144,
		136, 70, 238, 184, 20, 222, 94, 11, 219, 224, 50, 58, 10, 73, 6, 36,
		92, 194, 211, 172, 98, 145, 149, 228, 121, 231, 200, 55, 109, 141, 213,
		78, 169, 108, 86, 244, 234, 101, 122, 174, 8, 186, 120, 37, 46, 28,
		166, 180, 198, 232, 221, 116, 31, 75, 189, 139, 138, 112, 62, 181, 102,
		72, 3, 246, 14, 97, 53, 87, 185, 134, 193, 29, 158, 225, 248, 152, 17,
		105, 217, 142, 148, 155, 30, 135, 233, 206, 85, 40, 223, 140, 161, 137,
		13, 191, 230, 66, 104, 65, 153, 45, 15, 176, 84, 187, 22 ];
var SBoxInverse = [ 82, 9, 106, 213, 48, 54, 165, 56, 191, 64, 163, 158, 129,
		243, 215, 251, 124, 227, 57, 130, 155, 47, 255, 135, 52, 142, 67, 68,
		196, 222, 233, 203, 84, 123, 148, 50, 166, 194, 35, 61, 238, 76, 149,
		11, 66, 250, 195, 78, 8, 46, 161, 102, 40, 217, 36, 178, 118, 91, 162,
		73, 109, 139, 209, 37, 114, 248, 246, 100, 134, 104, 152, 22, 212, 164,
		92, 204, 93, 101, 182, 146, 108, 112, 72, 80, 253, 237, 185, 218, 94,
		21, 70, 87, 167, 141, 157, 132, 144, 216, 171, 0, 140, 188, 211, 10,
		247, 228, 88, 5, 184, 179, 69, 6, 208, 44, 30, 143, 202, 63, 15, 2,
		193, 175, 189, 3, 1, 19, 138, 107, 58, 145, 17, 65, 79, 103, 220, 234,
		151, 242, 207, 206, 240, 180, 230, 115, 150, 172, 116, 34, 231, 173,
		53, 133, 226, 249, 55, 232, 28, 117, 223, 110, 71, 241, 26, 113, 29,
		41, 197, 137, 111, 183, 98, 14, 170, 24, 190, 27, 252, 86, 62, 75, 198,
		210, 121, 32, 154, 219, 192, 254, 120, 205, 90, 244, 31, 221, 168, 51,
		136, 7, 199, 49, 177, 18, 16, 89, 39, 128, 236, 95, 96, 81, 127, 169,
		25, 181, 74, 13, 45, 229, 122, 159, 147, 201, 156, 239, 160, 224, 59,
		77, 174, 42, 245, 176, 200, 235, 187, 60, 131, 83, 153, 97, 23, 43, 4,
		126, 186, 119, 214, 38, 225, 105, 20, 99, 85, 33, 12, 125 ];
var Nk = keySizeInBits / 32;
var Nb = blockSizeInBits / 32;
var Nr = roundsArray[Nk][Nb];