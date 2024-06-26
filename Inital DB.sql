USE [master]
GO
/****** Object:  Database [Zavrsni]    Script Date: 22/10/2022 11:33:54 ******/
CREATE DATABASE [Zavrsni]
 CONTAINMENT = NONE
 ON  PRIMARY 
( NAME = N'Zavrsni', FILENAME = N'C:\Program Files\Microsoft SQL Server\MSSQL15.SQLEXPRESS\MSSQL\DATA\Zavrsni.mdf' , SIZE = 8192KB , MAXSIZE = UNLIMITED, FILEGROWTH = 65536KB )
 LOG ON 
( NAME = N'Zavrsni_log', FILENAME = N'C:\Program Files\Microsoft SQL Server\MSSQL15.SQLEXPRESS\MSSQL\DATA\Zavrsni_log.ldf' , SIZE = 8192KB , MAXSIZE = 2048GB , FILEGROWTH = 65536KB )
 WITH CATALOG_COLLATION = DATABASE_DEFAULT
GO
ALTER DATABASE [Zavrsni] SET COMPATIBILITY_LEVEL = 150
GO
IF (1 = FULLTEXTSERVICEPROPERTY('IsFullTextInstalled'))
begin
EXEC [Zavrsni].[dbo].[sp_fulltext_database] @action = 'enable'
end
GO
ALTER DATABASE [Zavrsni] SET ANSI_NULL_DEFAULT OFF 
GO
ALTER DATABASE [Zavrsni] SET ANSI_NULLS OFF 
GO
ALTER DATABASE [Zavrsni] SET ANSI_PADDING OFF 
GO
ALTER DATABASE [Zavrsni] SET ANSI_WARNINGS OFF 
GO
ALTER DATABASE [Zavrsni] SET ARITHABORT OFF 
GO
ALTER DATABASE [Zavrsni] SET AUTO_CLOSE OFF 
GO
ALTER DATABASE [Zavrsni] SET AUTO_SHRINK OFF 
GO
ALTER DATABASE [Zavrsni] SET AUTO_UPDATE_STATISTICS ON 
GO
ALTER DATABASE [Zavrsni] SET CURSOR_CLOSE_ON_COMMIT OFF 
GO
ALTER DATABASE [Zavrsni] SET CURSOR_DEFAULT  GLOBAL 
GO
ALTER DATABASE [Zavrsni] SET CONCAT_NULL_YIELDS_NULL OFF 
GO
ALTER DATABASE [Zavrsni] SET NUMERIC_ROUNDABORT OFF 
GO
ALTER DATABASE [Zavrsni] SET QUOTED_IDENTIFIER OFF 
GO
ALTER DATABASE [Zavrsni] SET RECURSIVE_TRIGGERS OFF 
GO
ALTER DATABASE [Zavrsni] SET  DISABLE_BROKER 
GO
ALTER DATABASE [Zavrsni] SET AUTO_UPDATE_STATISTICS_ASYNC OFF 
GO
ALTER DATABASE [Zavrsni] SET DATE_CORRELATION_OPTIMIZATION OFF 
GO
ALTER DATABASE [Zavrsni] SET TRUSTWORTHY OFF 
GO
ALTER DATABASE [Zavrsni] SET ALLOW_SNAPSHOT_ISOLATION OFF 
GO
ALTER DATABASE [Zavrsni] SET PARAMETERIZATION SIMPLE 
GO
ALTER DATABASE [Zavrsni] SET READ_COMMITTED_SNAPSHOT OFF 
GO
ALTER DATABASE [Zavrsni] SET HONOR_BROKER_PRIORITY OFF 
GO
ALTER DATABASE [Zavrsni] SET RECOVERY SIMPLE 
GO
ALTER DATABASE [Zavrsni] SET  MULTI_USER 
GO
ALTER DATABASE [Zavrsni] SET PAGE_VERIFY CHECKSUM  
GO
ALTER DATABASE [Zavrsni] SET DB_CHAINING OFF 
GO
ALTER DATABASE [Zavrsni] SET FILESTREAM( NON_TRANSACTED_ACCESS = OFF ) 
GO
ALTER DATABASE [Zavrsni] SET TARGET_RECOVERY_TIME = 60 SECONDS 
GO
ALTER DATABASE [Zavrsni] SET DELAYED_DURABILITY = DISABLED 
GO
ALTER DATABASE [Zavrsni] SET ACCELERATED_DATABASE_RECOVERY = OFF  
GO
ALTER DATABASE [Zavrsni] SET QUERY_STORE = OFF
GO
USE [Zavrsni]
GO
/****** Object:  Table [dbo].[Appointment]    Script Date: 22/10/2022 11:33:54 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Appointment](
	[Id] [int] IDENTITY(1,1) NOT NULL,
	[DateCreated] [datetime2](7) NOT NULL,
	[DateModified] [datetime2](7) NOT NULL,
	[DateDeleted] [datetime2](7) NULL,
	[Deleted] [bit] NOT NULL,
	[ShopId] [int] NOT NULL,
	[Date] [datetime2](7) NOT NULL,
	[IsTaken] [bit] NOT NULL,
 CONSTRAINT [PK_Appointment] PRIMARY KEY CLUSTERED 
(
	[Id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Car]    Script Date: 22/10/2022 11:33:54 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Car](
	[Id] [int] IDENTITY(1,1) NOT NULL,
	[DateCreated] [datetime2](7) NOT NULL,
	[DateModified] [datetime2](7) NOT NULL,
	[DateDeleted] [datetime2](7) NULL,
	[Deleted] [bit] NOT NULL,
	[UserId] [int] NOT NULL,
	[Manufacturer] [nvarchar](50) NOT NULL,
	[Model] [nvarchar](50) NOT NULL,
	[Year] [int] NOT NULL,
	[Odometer] [decimal](18, 0) NOT NULL,
 CONSTRAINT [PK_Car] PRIMARY KEY CLUSTERED 
(
	[Id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Location]    Script Date: 22/10/2022 11:33:54 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Location](
	[Id] [int] IDENTITY(1,1) NOT NULL,
	[DateCreated] [datetime2](7) NOT NULL,
	[DateModified] [datetime2](7) NOT NULL,
	[DateDeleted] [datetime2](7) NULL,
	[Deleted] [bit] NOT NULL,
	[UserId] [int] NULL,
	[Street] [nvarchar](50) NOT NULL,
	[StreetNumber] [nvarchar](50) NOT NULL,
	[City] [nvarchar](50) NOT NULL,
	[County] [nvarchar](50) NOT NULL,
	[Country] [nvarchar](50) NOT NULL,
	[Longitude] [float] NULL,
	[Latitude] [float] NULL,
 CONSTRAINT [PK_Location] PRIMARY KEY CLUSTERED 
(
	[Id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Log]    Script Date: 22/10/2022 11:33:54 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Log](
	[Id] [int] IDENTITY(1,1) NOT NULL,
	[ShopId] [int] NULL,
	[UserId] [int] NULL,
	[Timestamp] [datetime2](7) NOT NULL,
	[Application] [nvarchar](200) NOT NULL,
	[Source] [nvarchar](200) NOT NULL,
	[Severity] [nvarchar](200) NOT NULL,
	[Message] [nvarchar](max) NOT NULL,
 CONSTRAINT [PK_Log] PRIMARY KEY CLUSTERED 
(
	[Id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Request]    Script Date: 22/10/2022 11:33:54 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Request](
	[Id] [int] IDENTITY(1,1) NOT NULL,
	[DateCreated] [datetime2](7) NOT NULL,
	[DateModified] [datetime2](7) NOT NULL,
	[DateDeleted] [datetime2](7) NULL,
	[Deleted] [bit] NOT NULL,
	[CarId] [int] NOT NULL,
	[UserId] [int] NOT NULL,
	[ShopId] [int] NOT NULL,
	[RequestDate] [datetime2](7) NOT NULL,
	[RepairDate] [datetime2](7) NULL,
	[IssueDescription] [nvarchar](2000) NOT NULL,
	[EstimatedPrice] [decimal](18, 0) NULL,
	[Price] [decimal](18, 0) NULL,
	[EstimatedRepairHours] [int] NULL,
	[FinishDate] [datetime2](7) NULL,
	[BillPicture] [nvarchar](max) NULL,
	[Completed] [bit] NOT NULL,
	[Accepted] [bit] NOT NULL,
 CONSTRAINT [PK_Request] PRIMARY KEY CLUSTERED 
(
	[Id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Review]    Script Date: 22/10/2022 11:33:54 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Review](
	[Id] [int] IDENTITY(1,1) NOT NULL,
	[DateCreated] [datetime2](7) NOT NULL,
	[DateModified] [datetime2](7) NOT NULL,
	[DateDeleted] [datetime2](7) NULL,
	[Deleted] [bit] NOT NULL,
	[UserId] [int] NOT NULL,
	[ShopId] [int] NOT NULL,
	[Comment] [nvarchar](2000) NULL,
	[Rating] [decimal](18, 0) NOT NULL,
 CONSTRAINT [PK_Review] PRIMARY KEY CLUSTERED 
(
	[Id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Shop]    Script Date: 22/10/2022 11:33:54 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Shop](
	[Id] [int] IDENTITY(1,1) NOT NULL,
	[DateCreated] [datetime2](7) NOT NULL,
	[DateModified] [datetime2](7) NOT NULL,
	[DateDeleted] [datetime2](7) NULL,
	[Deleted] [bit] NOT NULL,
	[Vat] [nvarchar](50) NOT NULL,
	[LegalName] [nvarchar](200) NOT NULL,
	[ShortName] [nvarchar](50) NOT NULL,
	[LocationId] [int] NOT NULL,
	[Email] [nvarchar](50) NOT NULL,
	[Password] [nvarchar](100) NOT NULL,
	[ParentShopId] [int] NULL,
	[WorkHours] [nvarchar](11) NOT NULL,
	[WorkDays] [nvarchar](7) NOT NULL,
 CONSTRAINT [PK_Shop] PRIMARY KEY CLUSTERED 
(
	[Id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Token]    Script Date: 22/10/2022 11:33:54 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Token](
	[Token] [nvarchar](200) NOT NULL,
	[UserId] [int] NULL,
	[ShopId] [int] NULL,
 CONSTRAINT [PK_Token_1] PRIMARY KEY CLUSTERED 
(
	[Token] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[User]    Script Date: 22/10/2022 11:33:54 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[User](
	[Id] [int] IDENTITY(1,1) NOT NULL,
	[DateCreated] [datetime2](7) NOT NULL,
	[DateModified] [datetime2](7) NOT NULL,
	[DateDeleted] [datetime2](7) NULL,
	[Deleted] [bit] NOT NULL,
	[Username] [nvarchar](50) NOT NULL,
	[Password] [nvarchar](100) NOT NULL,
	[Email] [nvarchar](50) NOT NULL,
	[FirstName] [nvarchar](50) NOT NULL,
	[LastName] [nvarchar](50) NOT NULL,
	[DateOfBirth] [date] NOT NULL,
 CONSTRAINT [PK_User] PRIMARY KEY CLUSTERED 
(
	[Id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
ALTER TABLE [dbo].[Appointment] ADD  CONSTRAINT [DF_Appointment_Deleted]  DEFAULT ((0)) FOR [Deleted]
GO
ALTER TABLE [dbo].[Appointment] ADD  CONSTRAINT [DF_Appointment_IsTaken]  DEFAULT ((0)) FOR [IsTaken]
GO
ALTER TABLE [dbo].[Car] ADD  CONSTRAINT [DF_Car_Deleted]  DEFAULT ((0)) FOR [Deleted]
GO
ALTER TABLE [dbo].[Location] ADD  CONSTRAINT [DF_Location_Deleted]  DEFAULT ((0)) FOR [Deleted]
GO
ALTER TABLE [dbo].[Request] ADD  CONSTRAINT [DF_Request_Deleted]  DEFAULT ((0)) FOR [Deleted]
GO
ALTER TABLE [dbo].[Request] ADD  CONSTRAINT [DF_Request_Completed]  DEFAULT ((0)) FOR [Completed]
GO
ALTER TABLE [dbo].[Request] ADD  CONSTRAINT [DF_Request_Accepted]  DEFAULT ((0)) FOR [Accepted]
GO
ALTER TABLE [dbo].[Review] ADD  CONSTRAINT [DF_Review_Deleted]  DEFAULT ((0)) FOR [Deleted]
GO
ALTER TABLE [dbo].[Shop] ADD  CONSTRAINT [DF_Shop_Deleted]  DEFAULT ((0)) FOR [Deleted]
GO
ALTER TABLE [dbo].[Shop] ADD  CONSTRAINT [DF_Shop_workHours]  DEFAULT ('08:00-16:00') FOR [WorkHours]
GO
ALTER TABLE [dbo].[Shop] ADD  CONSTRAINT [DF_Shop_workDays]  DEFAULT ((1111111)) FOR [WorkDays]
GO
ALTER TABLE [dbo].[User] ADD  CONSTRAINT [DF_Table_1_deleted]  DEFAULT ((0)) FOR [Deleted]
GO
ALTER TABLE [dbo].[Appointment]  WITH CHECK ADD  CONSTRAINT [FK_Appointment_Shop] FOREIGN KEY([ShopId])
REFERENCES [dbo].[Shop] ([Id])
GO
ALTER TABLE [dbo].[Appointment] CHECK CONSTRAINT [FK_Appointment_Shop]
GO
ALTER TABLE [dbo].[Car]  WITH CHECK ADD  CONSTRAINT [FK_Car_User] FOREIGN KEY([UserId])
REFERENCES [dbo].[User] ([Id])
GO
ALTER TABLE [dbo].[Car] CHECK CONSTRAINT [FK_Car_User]
GO
ALTER TABLE [dbo].[Location]  WITH CHECK ADD  CONSTRAINT [FK_Location_User] FOREIGN KEY([UserId])
REFERENCES [dbo].[User] ([Id])
GO
ALTER TABLE [dbo].[Location] CHECK CONSTRAINT [FK_Location_User]
GO
ALTER TABLE [dbo].[Request]  WITH CHECK ADD  CONSTRAINT [FK_Request_Car] FOREIGN KEY([CarId])
REFERENCES [dbo].[Car] ([Id])
GO
ALTER TABLE [dbo].[Request] CHECK CONSTRAINT [FK_Request_Car]
GO
ALTER TABLE [dbo].[Request]  WITH CHECK ADD  CONSTRAINT [FK_Request_Shop] FOREIGN KEY([ShopId])
REFERENCES [dbo].[Shop] ([Id])
GO
ALTER TABLE [dbo].[Request] CHECK CONSTRAINT [FK_Request_Shop]
GO
ALTER TABLE [dbo].[Request]  WITH CHECK ADD  CONSTRAINT [FK_Request_User] FOREIGN KEY([UserId])
REFERENCES [dbo].[User] ([Id])
GO
ALTER TABLE [dbo].[Request] CHECK CONSTRAINT [FK_Request_User]
GO
ALTER TABLE [dbo].[Review]  WITH CHECK ADD  CONSTRAINT [FK_Review_Shop] FOREIGN KEY([ShopId])
REFERENCES [dbo].[Shop] ([Id])
GO
ALTER TABLE [dbo].[Review] CHECK CONSTRAINT [FK_Review_Shop]
GO
ALTER TABLE [dbo].[Review]  WITH CHECK ADD  CONSTRAINT [FK_Review_User] FOREIGN KEY([UserId])
REFERENCES [dbo].[User] ([Id])
GO
ALTER TABLE [dbo].[Review] CHECK CONSTRAINT [FK_Review_User]
GO
ALTER TABLE [dbo].[Shop]  WITH CHECK ADD  CONSTRAINT [FK_Shop_Location] FOREIGN KEY([LocationId])
REFERENCES [dbo].[Location] ([Id])
GO
ALTER TABLE [dbo].[Shop] CHECK CONSTRAINT [FK_Shop_Location]
GO
ALTER TABLE [dbo].[Shop]  WITH CHECK ADD  CONSTRAINT [FK_Shop_Shop] FOREIGN KEY([ParentShopId])
REFERENCES [dbo].[Shop] ([Id])
GO
ALTER TABLE [dbo].[Shop] CHECK CONSTRAINT [FK_Shop_Shop]
GO
ALTER TABLE [dbo].[Token]  WITH CHECK ADD  CONSTRAINT [FK_Token_Shop] FOREIGN KEY([ShopId])
REFERENCES [dbo].[Shop] ([Id])
GO
ALTER TABLE [dbo].[Token] CHECK CONSTRAINT [FK_Token_Shop]
GO
ALTER TABLE [dbo].[Token]  WITH CHECK ADD  CONSTRAINT [FK_Token_User] FOREIGN KEY([UserId])
REFERENCES [dbo].[User] ([Id])
GO
ALTER TABLE [dbo].[Token] CHECK CONSTRAINT [FK_Token_User]
GO
USE [master]
GO
ALTER DATABASE [Zavrsni] SET  READ_WRITE 
GO
